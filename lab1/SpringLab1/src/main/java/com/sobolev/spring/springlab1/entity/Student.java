package com.sobolev.spring.springlab1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "student")
@Getter
@Setter
public class Student {

    @Id
    @Column(name = "student_number")
    private String studentNumber;

    private String fullname;

    private String email;

    @Column(name = "id_group")
    private Long groupId;
}
