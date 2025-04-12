package com.sobolev.spring.springlab1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "shedule")
@Getter
@Setter
public class Schedule {
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_lecture")
    private Lecture lecture;

    @Column(name = "id_group")
    private Long groupId;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @Column(name = "location")
    private String location;
}
