package com.sobolev.spring.springlab1.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FullStudentAttendanceDTO {
    private String studentNumber;
    private String fullName;
    private String email;
    private String groupName;
    private Double attendancePercent;
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private String searchTerm;
}
