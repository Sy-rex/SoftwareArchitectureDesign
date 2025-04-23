package com.sobolev.spring.springlab2.service;

import com.sobolev.spring.springlab2.dto.CourseLectureReportProjection;
import com.sobolev.spring.springlab2.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {
    private final CourseRepository courseRepo;

    public ReportService(CourseRepository courseRepo) {
        this.courseRepo = courseRepo;
    }

    public List<CourseLectureReportProjection> getCourseReport(
            Long courseId, int semester, int year) {

        LocalDateTime start, end;

        if (semester == 1) {
            start = LocalDate.of(year, 9, 1).atStartOfDay();
            end   = LocalDate.of(year, 12, 31).atTime(23,59,59);
        } else {
            start = LocalDate.of(year + 1, 2,  1).atStartOfDay();
            end   = LocalDate.of(year + 1, 6, 30).atTime(23,59,59);
        }

        return courseRepo.findLectureAttendanceReport(courseId, start, end);
    }
}
