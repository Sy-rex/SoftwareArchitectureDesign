package com.sobolev.spring.springlab2.service;

import com.sobolev.spring.springlab2.dto.LectureReportDTO;
import com.sobolev.spring.springlab2.entity.Course;
import com.sobolev.spring.springlab2.entity.Lecture;
import com.sobolev.spring.springlab2.entity.UniversityDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final PostgresService postgresService;
    private final Neo4jService neo4jService;
    private final MongoService mongoService;

    public List<LectureReportDTO> generateReport(Long courseId, int semester, int studyYear) {
        log.info("generateReport invoked: courseId={}, semester={}, studyYear={}", courseId, semester, studyYear);

        Course course = postgresService.getCourseById(courseId);
        log.debug("Course loaded: id={}, name={}, deptId={}",
                course.getId(), course.getName(), course.getDepartmentId());

        List<Lecture> allLectures = postgresService.getLecturesByCourseId(courseId);
        log.debug("Total lectures from Postgres: {}", allLectures.size());

        Set<Long> scheduledIds = neo4jService.getScheduledLectureIds(semester, studyYear);
        log.debug("Lecture IDs scheduled in Neo4j: {}", scheduledIds);

        UniversityDocument uniDoc = mongoService.getUniversityByDepartmentId(course.getDepartmentId());
        if (uniDoc == null) {
            log.warn("No UniversityDocument found for departmentId={}", course.getDepartmentId());
        } else {
            log.debug("UniversityDocument fetched: universityId={}, name={}",
                    uniDoc.getUniversityId(), uniDoc.getName());
        }

        Optional<AbstractMap.SimpleEntry<String,String>> departmentInfo =
                Optional.ofNullable(uniDoc).flatMap(doc ->
                        doc.getInstitutes().stream()
                                .flatMap(inst -> inst.getDepartments().stream()
                                        .filter(d -> d.getDepartmentId().equals(course.getDepartmentId().intValue()))
                                        .map(d -> new AbstractMap.SimpleEntry<>(inst.getName(), d.getName()))
                                )
                                .findFirst()
                );

        String instName = departmentInfo.map(AbstractMap.SimpleEntry::getKey).orElse("(не найдено)");
        String deptName = departmentInfo.map(AbstractMap.SimpleEntry::getValue).orElse("(не найдено)");
        log.debug("Resolved institute='{}', department='{}'", instName, deptName);

        List<LectureReportDTO> report = allLectures.stream()
                .filter(lec -> scheduledIds.contains(lec.getId()))
                .peek(lec -> log.debug("Including lecture id={} name='{}'", lec.getId(), lec.getName()))
                .map(lec -> {
                    long studentCount = neo4jService.getStudentCount(lec.getId(), semester, studyYear);
                    log.debug("Lecture id={} has studentCount={}", lec.getId(), studentCount);
                    return new LectureReportDTO(course.getName(),
                            lec.getId(), lec.getName(), lec.getTechEquipment(),
                            studentCount,
                            uniDoc.getName(),
                            instName, deptName,
                            semester, studyYear
                    );
                })
                .collect(Collectors.toList());

        log.info("generateReport completed: returning {} entries", report.size());
        return report;
    }
}
