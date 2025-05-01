package com.sobolev.spring.springlab3.dto;

import lombok.Data;

@Data
public class ReportDTO {
    private String groupName;
    private String studentNumber;
    private String studentName;
    private String email;
    private String university;
    private String institute;
    private String department;
    private Integer plannedHours;
    private Integer attendedHours;
}
