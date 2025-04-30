package com.sobolev.spring.springlab2.service;

import com.sobolev.spring.springlab2.entity.Course;
import com.sobolev.spring.springlab2.entity.Lecture;
import com.sobolev.spring.springlab2.repository.CourseRepository;
import com.sobolev.spring.springlab2.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostgresService {

    private final CourseRepository courseRepository;
    private final LectureRepository lectureRepository;

    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));
    }

    public List<Lecture> getLecturesByCourseId(Long courseId) {
        return lectureRepository.findByCourseId(courseId);
    }
}
