package com.sobolev.spring.springlab3.repository;

import com.sobolev.spring.springlab3.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}
