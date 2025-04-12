package com.sobolev.spring.springlab1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "attendance")
@IdClass(AttendanceId.class)
@Getter
@Setter
public class Attendance {

    @Id
    private Long id;

    @Id
    private LocalDate weekStart;

    @Column(nullable = false)
    private Timestamp timestamp;

    @Column(name = "id_student", nullable = false)
    private String studentNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_schedule", nullable = false)
    private Schedule schedule;

    @Column(nullable = false)
    private Boolean status;
}
