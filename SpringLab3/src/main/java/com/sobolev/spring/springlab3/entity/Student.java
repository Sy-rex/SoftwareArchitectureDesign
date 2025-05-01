package com.sobolev.spring.springlab3.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "student")
@Getter
@Setter
public class Student {
    @Id
    private String studentNumber;

    private String fullname;

    private String email;

    private String redisKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_group")
    private Group group;
}
