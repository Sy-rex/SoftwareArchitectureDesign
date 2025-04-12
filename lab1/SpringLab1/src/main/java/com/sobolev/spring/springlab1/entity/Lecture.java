package com.sobolev.spring.springlab1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "lecture")
@Getter
@Setter
public class Lecture {

    @Id
    private Long id;

    private String name;

    @Column(name = "id_course")
    private Long courseId;

    @Column(name = "elasticsearch_id")
    private String elasticsearchId;
}
