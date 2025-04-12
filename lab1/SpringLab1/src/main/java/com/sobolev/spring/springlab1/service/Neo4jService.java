package com.sobolev.spring.springlab1.service;

import com.sobolev.spring.springlab1.dto.NeoLectureInfoDTO;
import lombok.RequiredArgsConstructor;
import org.neo4j.driver.Record;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class Neo4jService {

    private final Driver neo4jDriver;

    public Optional<Long> getDepartmentIdByStudentNumber(String studentNumber) {
        try (Session session = neo4jDriver.session()) {
            String cypher = """
            MATCH (s:Student {student_number: $studentNumber})-[:BELONGS_TO]->(:Group)
                  -[:HAS_SCHEDULE]->(:Lecture)-[:ORIGINATES_FROM]->(d:Department)
            RETURN d.department_id AS departmentId
            LIMIT 1
        """;

            return session.readTransaction(tx -> {
                Result result = tx.run(cypher, Values.parameters("studentNumber", studentNumber));
                return result.hasNext()
                        ? Optional.of(result.next().get("departmentId").asLong())
                        : Optional.empty();
            });
        }
    }

    public List<NeoLectureInfoDTO> getLecturesForStudent(String studentNumber) {
        try (Session session = neo4jDriver.session()) {
            String cypher = """
                MATCH (s:Student {student_number: $studentNumber})-[:BELONGS_TO]->(:Group)
                      -[:HAS_SCHEDULE]->(l:Lecture)-[:ORIGINATES_FROM]->(d:Department)
                RETURN DISTINCT l.name AS lectureName, d.name AS departmentName
            """;

            return session.readTransaction(tx -> {
                Result result = tx.run(cypher, Values.parameters("studentNumber", studentNumber));
                List<NeoLectureInfoDTO> lectures = new ArrayList<>();
                while (result.hasNext()) {
                    Record record = result.next();
                    NeoLectureInfoDTO dto = new NeoLectureInfoDTO();
                    dto.setLectureName(record.get("lectureName").asString());
                    dto.setDepartmentName(record.get("departmentName").asString());
                    lectures.add(dto);
                }
                return lectures;
            });
        }
    }
}
