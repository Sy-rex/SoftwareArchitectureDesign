package com.sobolev.spring.springlab3.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance")
@Getter
@Setter
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "week_start", nullable = false)
    private LocalDate weekStart;

    @Column(name = "id_student", nullable = false)
    private String studentNumber;

    @Column(name = "id_schedule", nullable = false)
    private Long scheduleId;

    @Column(nullable = false)
    private Boolean status = true;
}
