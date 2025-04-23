package com.sobolev.spring.springlab2.dto;

public interface CourseLectureReportProjection {
    Long   getCourseId();
    String getCourseName();
    Long   getLectureId();
    String getLectureName();
    Boolean getTechEquipment();
    Long   getListenerCount();
}
