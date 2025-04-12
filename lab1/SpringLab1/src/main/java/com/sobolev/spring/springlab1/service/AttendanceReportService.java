package com.sobolev.spring.springlab1.service;

import com.sobolev.spring.springlab1.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceReportService {

    private final LectureMaterialService lectureMaterialService;
    private final PostgresAttendanceService attendanceService;
    private final RedisService redisService;
    private final MongoUniversityService mongoUniversityService;
    private final Neo4jService neo4jService;

    public List<FullStudentAttendanceDTO> generateReport(String term, LocalDateTime from, LocalDateTime to) {
        List<Long> lectureIds = lectureMaterialService.searchLectureIds(term);
        if (lectureIds.isEmpty()) {
            log.warn("Ни одной лекции не найдено по термину '{}'", term);
            return List.of();
        }

        List<StudentAttendanceRawDTO> rawAttendance = attendanceService
                .getTop10WithLowestAttendance(lectureIds, from, to);

        return rawAttendance.stream()
                .map(raw -> {
                    String studentNumber = raw.getStudentNumber();

                    RedisStudentInfo redisInfo;
                    try {
                        redisInfo = redisService.getStudentInfo(studentNumber);
                    } catch (Exception e) {
                        log.warn("Ошибка при получении Redis-инфо о студенте {}: {}", studentNumber, e.getMessage());
                        return null;
                    }

                    // --- Получение кафедры ---
                    Optional<Long> departmentIdOpt = neo4jService.getDepartmentIdByStudentNumber(studentNumber);

                    MongoGroupInfo hierarchy = departmentIdOpt
                            .map(id -> {
                                try {
                                    return mongoUniversityService.getHierarchyByDepartmentId(id);
                                } catch (Exception e) {
                                    log.warn("Не найдена кафедра в MongoDB по departmentId {}: {}", id, e.getMessage());
                                    return MongoGroupInfo.unknown();
                                }
                            })
                            .orElseGet(() -> {
                                log.warn("Кафедра не найдена в Neo4j для студента {}", studentNumber);
                                return MongoGroupInfo.unknown();
                            });

                    // --- Получение связанных лекций ---
                    List<String> relatedLectures = neo4jService.getLecturesForStudent(studentNumber).stream()
                            .map(NeoLectureInfoDTO::getLectureName)
                            .filter(Objects::nonNull)
                            .distinct()
                            .toList();

                    return FullStudentAttendanceDTO.builder()
                            .studentNumber(studentNumber)
                            .fullName(redisInfo.getFullname())
                            .email(redisInfo.getEmail())
                            .groupName(redisInfo.getGroupName())
                            .university(hierarchy.getUniversity())
                            .institute(hierarchy.getInstitute())
                            .department(hierarchy.getDepartment())
                            .attendancePercent(raw.getAttendancePercent())
                            .periodStart(raw.getPeriodStart().toLocalDateTime())
                            .periodEnd(raw.getPeriodEnd().toLocalDateTime())
                            .searchTerm(term)
                            .relatedLectures(relatedLectures)
                            .build();
                })
                .filter(Objects::nonNull)
                .toList();
    }
}
