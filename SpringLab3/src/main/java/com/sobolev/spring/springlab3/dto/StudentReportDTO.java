package com.sobolev.spring.springlab3.dto;

import lombok.Data;


@Data
public class StudentReportDTO {
    private String studentNumber;
    private String fullname;
    private String groupName;
    private String courseName;
    private String departmentName;
    private Long plannedHours;
    private Long attendedHours;
}
