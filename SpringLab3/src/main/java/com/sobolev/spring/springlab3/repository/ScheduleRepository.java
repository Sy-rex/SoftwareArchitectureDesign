package com.sobolev.spring.springlab3.repository;

import com.sobolev.spring.springlab3.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("SELECT DISTINCT s.lecture.id FROM Schedule s " +
            "WHERE s.group.id = :groupId AND s.lecture.course.department.id <> :deptId")
    List<Long> findSpecialLectureIds(@Param("groupId") Long groupId,
                                     @Param("deptId") Long deptId);
}
