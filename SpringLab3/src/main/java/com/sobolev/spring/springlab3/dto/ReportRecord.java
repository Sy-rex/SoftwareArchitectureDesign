package com.sobolev.spring.springlab3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportRecord {
    private String studentNumber;
    private String courseName;
    private Long scheduledCount;
    private Long attendedCount;
}
