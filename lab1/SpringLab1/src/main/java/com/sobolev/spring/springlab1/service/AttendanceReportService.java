package com.sobolev.spring.springlab1.service;

import com.sobolev.spring.springlab1.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceReportService {

    private final LectureMaterialService lectureMaterialService;
    private final PostgresAttendanceService attendanceService;
    private final RedisService redisService;
    private final Neo4jService neo4jService;

    public List<FullStudentAttendanceDTO> generateReport(String term, LocalDateTime from, LocalDateTime to) {
        // Ищем лекции по термину
        List<Long> lectureIds = lectureMaterialService.searchLectureIds(term);
        if (lectureIds.isEmpty()) {
            log.warn("Ни одной лекции не найдено по термину '{}'", term);
            return Collections.emptyList();
        }

        // Получаем ожидаемые посещения из Neo4j: студент -> сколько должен был посетить занятий
        Map<String, Integer> expectedMap = neo4jService.getExpectedAttendanceCountMap(lectureIds, from, to);
        if (expectedMap.isEmpty()) {
            log.warn("Не найдено студентов в Neo4j по заданным лекциям и периоду");
            return Collections.emptyList();
        }

        // Получаем фактические посещения из Postgres: студент -> сколько реально посетил
        List<String> students = new ArrayList<>(expectedMap.keySet());
        Map<String, Integer> actualMap = attendanceService
                .getActualAttendanceCountMap(lectureIds, from, to, students);

        // Рассчитываем процент посещения и берём 10 студентов с минимальным значением
        List<StudentAttendancePercentDTO> percentList = expectedMap.entrySet().stream()
                .map(e -> {
                    String studentNumber = e.getKey();
                    int expected = e.getValue();
                    int actual = actualMap.getOrDefault(studentNumber, 0);
                    double percent = expected > 0 ? actual * 100.0 / expected : 0.0;
                    return new StudentAttendancePercentDTO(studentNumber, percent);
                })
                .sorted(Comparator.comparingDouble(StudentAttendancePercentDTO::getPercent))
                .limit(10)
                .collect(Collectors.toList());

        return percentList.stream()
                .map(p -> {
                    String studentNumber = p.getStudentNumber();
                    double attendancePercent = p.getPercent();

                    // Redis
                    RedisStudentInfo redisInfo;
                    try {
                        redisInfo = redisService.getStudentInfo(studentNumber);
                    } catch (Exception ex) {
                        log.warn("Ошибка при получении Redis-инфо о студенте {}: {}", studentNumber, ex.getMessage());
                        return null;
                    }

                    return FullStudentAttendanceDTO.builder()
                            .studentNumber(studentNumber)
                            .fullName(redisInfo.getFullname())
                            .email(redisInfo.getEmail())
                            .groupName(redisInfo.getGroupName())
                            .attendancePercent(attendancePercent)
                            .periodStart(from)
                            .periodEnd(to)
                            .searchTerm(term)
                            .build();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}