package com.sobolev.spring.springlab1.service;

import com.sobolev.spring.springlab1.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.sql.Timestamp;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostgresAttendanceService {

    private final AttendanceRepository attendanceRepository;

    public Map<String, Integer> getActualAttendanceCountMap(
            List<Long> lectureIds,
            LocalDateTime from,
            LocalDateTime to,
            List<String> studentNumbers
    ) {
        List<Object[]> rows = attendanceRepository
                .findActualAttendanceCountByLectureIdsAndPeriodAndStudents(
                        lectureIds,
                        Timestamp.valueOf(from),
                        Timestamp.valueOf(to),
                        studentNumbers
                );
        Map<String, Integer> actualMap = new HashMap<>();
        for (Object[] row : rows) {
            String studentNumber = (String) row[0];
            Integer count = ((Number) row[1]).intValue();
            actualMap.put(studentNumber, count);
        }
        return actualMap;
    }
}
