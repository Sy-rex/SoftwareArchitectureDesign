package com.sobolev.spring.springlab3.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Negative;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "department")
@Getter
@Setter
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
