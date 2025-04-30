package com.sobolev.spring.springlab2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.*;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class Neo4jService {

    private final Driver driver;

    public Set<Long> getScheduledLectureIds(int semester, int studyYear) {
        log.info("getScheduledLectureIds called: semester={}, studyYear={}", semester, studyYear);

        String cypher = """
            MATCH (g:Group)-[hs:HAS_SCHEDULE]->(l:Lecture)
            WHERE hs.date.year = $year AND (
              ($sem = 1 AND hs.date.month >= 9 AND hs.date.month <= 12) OR
              ($sem = 2 AND hs.date.month >= 1 AND hs.date.month <= 6)
            )
            RETURN DISTINCT l.id AS id
            """;

        Set<Long> ids = new HashSet<>();
        try (Session session = driver.session()) {
            var params = Values.parameters("year", studyYear, "sem", semester);
            Result rs = session.run(cypher, params);
            while (rs.hasNext()) {
                long id = rs.next().get("id").asLong();
                ids.add(id);
            }
        }

        log.debug("Scheduled lecture IDs from Neo4j: {}", ids);
        return ids;
    }

    public long getStudentCount(Long lectureId, int semester, int studyYear) {
        log.info("getStudentCount called: lectureId={}, semester={}, studyYear={}", lectureId, semester, studyYear);

        String cypher = """
            MATCH (l:Lecture {id: $lid})<-[hs:HAS_SCHEDULE]-(g:Group)<-[:BELONGS_TO]-(s:Student)
            WHERE hs.date.year = $year AND (
              ($sem = 1 AND hs.date.month >= 9 AND hs.date.month <= 12) OR
              ($sem = 2 AND hs.date.month >= 1 AND hs.date.month <= 6)
            )
            RETURN count(DISTINCT s) AS cnt
            """;

        try (Session session = driver.session()) {
            var params = Values.parameters(
                    "lid", lectureId,
                    "year", studyYear,
                    "sem", semester
            );
            Result rs = session.run(cypher, params);
            if (rs.hasNext()) {
                long cnt = rs.next().get("cnt").asLong();
                log.debug("Student count for lecture {}: {}", lectureId, cnt);
                return cnt;
            }
        }

        log.warn("No scheduled sessions found for lecture {} in sem={} year={}", lectureId, semester, studyYear);
        return 0L;
    }
}
