package com.sobolev.spring.springlab3.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "schedule")
@Getter
@Setter
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_lecture", nullable = false)
    private Long lectureId;

    @Column(name = "id_group", nullable = false)
    private Long groupId;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    private String location;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}