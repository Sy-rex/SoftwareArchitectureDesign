package com.sobolev.spring.springlab2.controller;

import com.sobolev.spring.springlab2.dto.LectureReportDTO;
import com.sobolev.spring.springlab2.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/course")
    public List<LectureReportDTO> getReport(
            @RequestParam Long courseId,
            @RequestParam int semester,
            @RequestParam int studyYear
    ) {
        return reportService.generateReport(courseId, semester, studyYear);
    }
}
