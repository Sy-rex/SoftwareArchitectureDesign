package com.sobolev.spring.springlab1.service;

import com.sobolev.spring.springlab1.dto.StudentAttendanceRawDTO;
import com.sobolev.spring.springlab1.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.sql.Timestamp;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostgresAttendanceService {

    private final AttendanceRepository attendanceRepository;

    public List<StudentAttendanceRawDTO> getTop10WithLowestAttendance(
            List<Long> lectureIds,
            LocalDateTime from,
            LocalDateTime to
    ) {
        System.out.println(from);
        System.out.println(to);
        List<Object[]> rows = attendanceRepository.findLowestAttendanceByLectureIdsAndPeriod(
                lectureIds,
                Timestamp.valueOf(from),
                Timestamp.valueOf(to)
        );

        return rows.stream()
                .map(row -> new StudentAttendanceRawDTO(
                        (String) row[0],
                        ((BigDecimal) row[1]).doubleValue()
                )).toList();
    }
}
