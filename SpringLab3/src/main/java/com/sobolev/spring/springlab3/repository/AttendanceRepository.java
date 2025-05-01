package com.sobolev.spring.springlab3.repository;

import com.sobolev.spring.springlab3.entity.Attendance;
import com.sobolev.spring.springlab3.entity.AttendanceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, AttendanceId> {
    @Query("""
        SELECT a.student.studentNumber, 
               SUM(CASE WHEN a.status = true THEN 2 ELSE 0 END)
        FROM Attendance a
        JOIN a.schedule sch
        JOIN sch.lecture l
        WHERE sch.group.id = :groupId
          AND l.id IN :lectureIds
        GROUP BY a.student.studentNumber
    """)
    List<Object[]> findAttendedHoursByGroupAndLectures(
            @Param("groupId") Long groupId,
            @Param("lectureIds") List<Long> lectureIds
    );
}
