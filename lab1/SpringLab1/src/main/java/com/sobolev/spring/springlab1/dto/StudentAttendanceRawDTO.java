package com.sobolev.spring.springlab1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentAttendanceRawDTO {
    private String studentNumber;
    private Double attendancePercent;
}
