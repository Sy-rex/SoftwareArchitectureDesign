package com.sobolev.spring.springlab2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "lecture")
@Getter @Setter
public class Lecture {
    @Id
    private Long id;

    private String name;

    @Column(name = "duration_hours")
    private Integer durationHours;

    @Column(name = "tech_equipment")
    private Boolean techEquipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_course", nullable = false)
    private Course course;

    @Column(name = "elasticsearch_id")
    private String elasticsearchId;
}