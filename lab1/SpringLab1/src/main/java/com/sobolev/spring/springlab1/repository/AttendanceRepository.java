package com.sobolev.spring.springlab1.repository;

import com.sobolev.spring.springlab1.entity.Attendance;
import com.sobolev.spring.springlab1.entity.AttendanceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, AttendanceId> {
    @Query(value = """
        SELECT a.id_student AS studentNumber,
               COUNT(CASE WHEN a.status THEN 1 END) AS actualCount
        FROM attendance a
        JOIN schedule sch ON a.id_schedule = sch.id
        JOIN lecture l ON sch.id_lecture = l.id
        WHERE l.id IN :lectureIds
          AND a.timestamp BETWEEN :from AND :to
          AND a.id_student IN :studentNumbers
        GROUP BY a.id_student
    """, nativeQuery = true)
    List<Object[]> findActualAttendanceCountByLectureIdsAndPeriodAndStudents(
            @Param("lectureIds") List<Long> lectureIds,
            @Param("from") Timestamp from,
            @Param("to") Timestamp to,
            @Param("studentNumbers") List<String> studentNumbers
    );
}