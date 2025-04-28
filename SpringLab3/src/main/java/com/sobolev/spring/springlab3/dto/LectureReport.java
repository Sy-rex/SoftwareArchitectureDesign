package com.sobolev.spring.springlab3.dto;

import lombok.Data;

@Data
public class LectureReport {
    private String lectureName;
    private String courseName;
    private int plannedHours;
    private int attendedHours;
}
