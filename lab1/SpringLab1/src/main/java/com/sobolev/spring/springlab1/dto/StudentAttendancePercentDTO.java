package com.sobolev.spring.springlab1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentAttendancePercentDTO {
    private String studentNumber;
    private Double percent;
}
