package com.sobolev.spring.springlab1.dto;

import lombok.Data;

@Data
public class LectureMaterialDTO {
    private Long id;
    private String name;
    private String description;
    private Long lectureId;
}
