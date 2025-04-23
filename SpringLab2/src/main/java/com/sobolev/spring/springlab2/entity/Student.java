package com.sobolev.spring.springlab2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "student")
@Getter @Setter
public class Student {
    @Id
    @Column(name = "student_number")
    private String studentNumber;

    private String fullname;

    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_group", nullable = false)
    private Group group;
}