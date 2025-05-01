package com.sobolev.spring.springlab3.entity;

import lombok.Data;

import java.util.List;

@Data
public class InstituteDocument {
    private Integer id;
    private String name;
    private List<DepartmentDocument> departments;
}