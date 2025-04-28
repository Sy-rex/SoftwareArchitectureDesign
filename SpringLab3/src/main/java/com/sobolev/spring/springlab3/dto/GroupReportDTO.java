package com.sobolev.spring.springlab3.dto;

import lombok.Data;

import java.util.List;

@Data
public class GroupReportDTO {
    private Long groupId;
    private String groupName;
    private String departmentName;
    private List<StudentReportDTO> students;
}
