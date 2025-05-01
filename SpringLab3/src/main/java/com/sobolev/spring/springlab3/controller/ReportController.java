package com.sobolev.spring.springlab3.controller;

import com.sobolev.spring.springlab3.dto.ReportDTO;
import com.sobolev.spring.springlab3.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/reports3")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/group/{groupId}")
    public List<ReportDTO> getReportByGroup(@PathVariable Long groupId) {
        return reportService.getReportByGroup(groupId);
    }
}
