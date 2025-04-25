package com.sobolev.spring.springlab2.controller;

import com.sobolev.spring.springlab2.dto.CourseLectureReportProjection;
import com.sobolev.spring.springlab2.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * GET /api/reports/course/{courseId}?semester=1&year=2023
     */
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<CourseLectureReportProjection>> getCourseReport(
            @PathVariable Long courseId,
            @RequestParam int semester,
            @RequestParam("year") int year) {

        List<CourseLectureReportProjection> report =
                reportService.getCourseReport(courseId, semester, year);

        if (report.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(report);
    }
}
