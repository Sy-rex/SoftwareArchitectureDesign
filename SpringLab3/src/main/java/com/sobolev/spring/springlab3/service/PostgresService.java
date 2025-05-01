package com.sobolev.spring.springlab3.service;

import com.sobolev.spring.springlab3.repository.AttendanceRepository;
import com.sobolev.spring.springlab3.repository.GroupRepository;
import com.sobolev.spring.springlab3.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostgresService {

    private final GroupRepository groupRepo;
    private final ScheduleRepository scheduleRepo;
    private final AttendanceRepository attendanceRepo;


    public List<Long> getSpecialLectureIds(Long groupId) {
        var group = groupRepo.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        Long deptId = group.getDepartment().getId();
        return scheduleRepo.findSpecialLectureIds(groupId, deptId);
    }

    public Map<String, Integer> getAttendedHours(Long groupId, List<Long> lectureIds) {
        Map<String, Integer> map = new HashMap<>();
        if (lectureIds == null || lectureIds.isEmpty()) return map;

        for (Object[] row : attendanceRepo.findAttendedHoursByGroupAndLectures(groupId, lectureIds)) {
            String student = (String) row[0];
            Integer hours  = ((Number) row[1]).intValue();
            map.put(student, hours);
        }
        return map;
    }
}
