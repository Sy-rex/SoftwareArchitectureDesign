package com.sobolev.spring.springlab3.repository;

import com.sobolev.spring.springlab3.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("SELECT s FROM Schedule s WHERE s.groupId = :groupId AND s.lectureId IN :lectureIds")
    List<Schedule> findByGroupIdAndLectureIds(Long groupId, List<Long> lectureIds);
}
