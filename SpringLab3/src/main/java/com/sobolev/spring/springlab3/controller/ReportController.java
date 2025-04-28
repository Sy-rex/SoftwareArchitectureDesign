package com.sobolev.spring.springlab3.controller;

import com.sobolev.spring.springlab3.dto.GroupReportDTO;
import com.sobolev.spring.springlab3.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/reports3")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/group/{groupId}")
    public ResponseEntity<GroupReportDTO> getGroupReport(@PathVariable Long groupId) {
        GroupReportDTO report = reportService.generateGroupReport(groupId);
        return ResponseEntity.ok(report);
    }
}
