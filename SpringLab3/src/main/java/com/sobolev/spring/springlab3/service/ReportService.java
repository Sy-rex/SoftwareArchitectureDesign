package com.sobolev.spring.springlab3.service;

import com.sobolev.spring.springlab3.dto.GroupReportDTO;
import com.sobolev.spring.springlab3.dto.StudentReportDTO;
import com.sobolev.spring.springlab3.entity.Attendance;
import com.sobolev.spring.springlab3.entity.Schedule;
import com.sobolev.spring.springlab3.entity.StudentNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final PostgresService postgresService;
    private final Neo4jService neo4jService;
    private final RedisService redisService;

    private static final int HOURS_PER_LECTURE = 2;

    public GroupReportDTO generateGroupReport(Long groupId) {
        GroupReportDTO report = new GroupReportDTO();
        report.setGroupId(groupId);

        // Получаем информацию о группе из Neo4j
        neo4jService.findGroupNodeById(groupId).ifPresent(groupNode -> {
            report.setGroupName(groupNode.getName());
            report.setDepartmentName(groupNode.getDepartment() != null ? groupNode.getDepartment().getName() : "Unknown");
        });

        // Получаем специальные лекции из Neo4j
        List<Long> specialLectureIds = neo4jService.findSpecialLectureIdsByGroupId(groupId);

        // Получаем расписание для специальных лекций из PostgreSQL
        List<Schedule> schedules = postgresService.findSchedulesByGroupIdAndLectureIds(groupId, specialLectureIds);

        // Получаем студентов группы из Neo4j
        List<StudentNode> students = neo4jService.findStudentsByGroupId(groupId);

        // Формируем отчет
        List<StudentReportDTO> studentReports = new ArrayList<>();
        Map<Long, Long> coursePlannedHours = new HashMap<>();
        Map<Long, Long> scheduleToCourseId = new HashMap<>();
        Map<Long, String> courseNames = new HashMap<>();

        // Собираем информацию о курсах и запланированных часах
        for (Schedule schedule : schedules) {
            Long lectureId = schedule.getLectureId();
            neo4jService.findLectureById(lectureId).ifPresent(lectureNode -> {
                Long courseId = lectureNode.getCourseId();
                if (courseId != null) {
                    postgresService.findCourseById(courseId).ifPresent(course -> {
                        courseNames.put(courseId, course.getName());
                        coursePlannedHours.put(courseId, coursePlannedHours.getOrDefault(courseId, 0L) + HOURS_PER_LECTURE);
                        scheduleToCourseId.put(schedule.getId(), courseId);
                    });
                }
            });
        }

        // Обрабатываем каждого студента
        for (StudentNode student : students) {
            String redisKey = student.getRedisKey();
            Map<String, String> studentDetails = redisService.getStudentDetails(redisKey);

            // Получаем посещаемость
            List<Long> scheduleIds = schedules.stream().map(Schedule::getId).collect(Collectors.toList());
            List<Attendance> attendances = postgresService.findAttendancesByStudentAndSchedules(student.getStudentNumber(), scheduleIds);

            // Группируем посещаемость по курсам
            Map<Long, Long> attendedHoursByCourse = new HashMap<>();
            for (Attendance attendance : attendances) {
                if (attendance.getStatus()) {
                    Long courseId = scheduleToCourseId.get(attendance.getScheduleId());
                    if (courseId != null) {
                        attendedHoursByCourse.put(courseId, attendedHoursByCourse.getOrDefault(courseId, 0L) + HOURS_PER_LECTURE);
                    }
                }
            }

            // Создаем отчет для каждого курса
            for (Map.Entry<Long, Long> entry : coursePlannedHours.entrySet()) {
                Long courseId = entry.getKey();
                Long plannedHours = entry.getValue();
                Long attendedHours = attendedHoursByCourse.getOrDefault(courseId, 0L);

                StudentReportDTO studentReport = new StudentReportDTO();
                studentReport.setStudentNumber(student.getStudentNumber());
                studentReport.setFullname(studentDetails.getOrDefault("fullname", student.getFullname()));
                studentReport.setGroupName(report.getGroupName());
                studentReport.setCourseName(courseNames.getOrDefault(courseId, "Unknown"));
                studentReport.setDepartmentName(report.getDepartmentName());
                studentReport.setPlannedHours(plannedHours);
                studentReport.setAttendedHours(attendedHours);

                studentReports.add(studentReport);
            }
        }

        report.setStudents(studentReports);
        return report;
    }
}
