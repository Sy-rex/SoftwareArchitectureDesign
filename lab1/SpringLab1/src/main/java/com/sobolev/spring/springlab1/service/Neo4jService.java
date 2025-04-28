package com.sobolev.spring.springlab1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Record;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class Neo4jService {

    private final Driver neo4jDriver;

    public Map<String, Integer> getExpectedAttendanceCountMap(List<Long> lectureIds,
                                                              LocalDateTime from,
                                                              LocalDateTime to) {
        String cypher = """
            MATCH (l:Lecture)<-[hs:HAS_SCHEDULE]-(g:Group)<-[:BELONGS_TO]-(s:Student)
            WHERE l.id IN $lectureIds
              AND hs.date >= datetime($from)
              AND hs.date <= datetime($to)
            RETURN s.student_number AS studentNumber,
                   COUNT(hs) AS expectedCount
        """;

        Map<String, Object> params = Map.of(
                "lectureIds", lectureIds,
                "from", from.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                "to", to.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );

        try (Session session = neo4jDriver.session()) {
            return session.readTransaction(tx -> {
                Result result = tx.run(cypher, params);
                Map<String, Integer> expectedMap = new HashMap<>();
                for (Record record : result.list()) {
                    String studentNumber = record.get("studentNumber").asString();
                    int count = record.get("expectedCount").asInt();
                    expectedMap.put(studentNumber, count);
                }
                return expectedMap;
            });
        } catch (Exception e) {
            log.error("Ошибка при запросе ожидаемого посещения из Neo4j: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }
}
