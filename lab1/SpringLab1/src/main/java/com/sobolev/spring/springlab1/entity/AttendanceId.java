package com.sobolev.spring.springlab1.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceId implements Serializable {
    private Long id;
    private LocalDate weekStart;
}
