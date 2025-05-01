package com.sobolev.spring.springlab3.service;

import com.sobolev.spring.springlab3.dto.ReportDTO;
import com.sobolev.spring.springlab3.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {
    private final PostgresService pg;
    private final Neo4jService neo4j;
    private final RedisService redis;
    private final MongoService mongo;
    private final GroupRepository groupRepo;

    public List<ReportDTO> getReportByGroup(Long groupId) {
        log.info("Starting report generation for group {}", groupId);

        // специальные лекции
        List<Long> specialIds = pg.getSpecialLectureIds(groupId);
        log.debug("Found {} special lectures: {}", specialIds.size(), specialIds);

        int plannedHours = neo4j.getPlannedHours(groupId, specialIds);
        log.debug("Planned hours for group {}: {}", groupId, plannedHours);

        // Посещённые часы по студентам
        Map<String, Integer> attended = pg.getAttendedHours(groupId, specialIds);
        log.debug("Attended hours per student: {} entries", attended.size());

        Set<String> students = new HashSet<>(attended.keySet());

        var group = groupRepo.findById(groupId)
                .orElseThrow(() -> {
                    log.error("Group {} not found", groupId);
                    return new RuntimeException("Group not found");
                });

        List<ReportDTO> report = new ArrayList<>();
        for (String stu : students) {
            ReportDTO dto = new ReportDTO();
            dto.setGroupName(group.getName());
            dto.setStudentNumber(stu);
            dto.setPlannedHours(plannedHours);
            dto.setAttendedHours(attended.getOrDefault(stu, 0));
            redis.enrichStudentInfo(dto, stu);
            mongo.enrichHierarchy(dto, group.getDepartment().getId());
            report.add(dto);
        }

        log.info("Report generation completed: {} records for group {}", report.size(), groupId);
        return report;
    }
}
