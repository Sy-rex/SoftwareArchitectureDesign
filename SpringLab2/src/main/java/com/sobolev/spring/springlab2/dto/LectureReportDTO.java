package com.sobolev.spring.springlab2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectureReportDTO {
    private String courseName;
    private Long lectureId;
    private String lectureName;
    private Boolean techEquipment;
    private Long studentCount;
    private String universityName;
    private String instituteName;
    private String departmentName;
    private int semester;
    private int studyYear;
}
