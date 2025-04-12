package com.sobolev.spring.springlab1.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class FullStudentAttendanceDTO {
    private String studentNumber;
    private String fullName;
    private String email;
    private String groupName;
    private String university;
    private String institute;
    private String department;
    private Double attendancePercent;
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private String searchTerm;
    private List<String> relatedLectures;
}
