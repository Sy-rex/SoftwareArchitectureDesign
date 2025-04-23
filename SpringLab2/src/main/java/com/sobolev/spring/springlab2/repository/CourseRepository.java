package com.sobolev.spring.springlab2.repository;

import com.sobolev.spring.springlab2.entity.Course;
import com.sobolev.spring.springlab2.dto.CourseLectureReportProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query(value = """
        SELECT
          c.id    AS courseId,
          c.name  AS courseName,
          l.id    AS lectureId,
          l.name  AS lectureName,
          l.tech_equipment AS techEquipment,
          COUNT(DISTINCT a.id_student) AS listenerCount
        FROM course c
        JOIN lecture l      ON c.id       = l.id_course
        JOIN schedule s     ON l.id       = s.id_lecture
        JOIN attendance a   ON s.id       = a.id_schedule
        WHERE c.id = :courseId
          AND s.timestamp BETWEEN :start AND :end
        GROUP BY c.id, c.name, l.id, l.name, l.tech_equipment
        """, nativeQuery = true)
    List<CourseLectureReportProjection> findLectureAttendanceReport(
            @Param("courseId") Long courseId,
            @Param("start")    LocalDateTime start,
            @Param("end")      LocalDateTime end
    );
}
