package com.sobolev.spring.springlab3.service;

import com.sobolev.spring.springlab3.entity.Attendance;
import com.sobolev.spring.springlab3.entity.Course;
import com.sobolev.spring.springlab3.entity.Schedule;
import com.sobolev.spring.springlab3.repository.AttendanceRepository;
import com.sobolev.spring.springlab3.repository.CourseRepository;
import com.sobolev.spring.springlab3.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostgresService {
    private final ScheduleRepository scheduleRepository;
    private final AttendanceRepository attendanceRepository;
    private final CourseRepository courseRepository;

    public List<Schedule> findSchedulesByGroupIdAndLectureIds(Long groupId, List<Long> lectureIds) {
        return scheduleRepository.findByGroupIdAndLectureIds(groupId, lectureIds);
    }

    public List<Attendance> findAttendancesByStudentAndSchedules(String studentNumber, List<Long> scheduleIds) {
        return attendanceRepository.findByStudentNumberAndScheduleIds(studentNumber, scheduleIds);
    }

    public Optional<Course> findCourseById(Long courseId) {
        return courseRepository.findById(courseId);
    }
}
