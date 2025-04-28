package com.sobolev.spring.springlab3.repository;

import com.sobolev.spring.springlab3.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    @Query("SELECT a FROM Attendance a WHERE a.studentNumber = :studentNumber AND a.scheduleId IN :scheduleIds")
    List<Attendance> findByStudentNumberAndScheduleIds(String studentNumber, List<Long> scheduleIds);
}
