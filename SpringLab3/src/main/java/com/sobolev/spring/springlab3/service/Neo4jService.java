package com.sobolev.spring.springlab3.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class Neo4jService {

    private final Neo4jClient client;

    @Transactional(readOnly = true)
    public int getPlannedHours(Long groupId, List<Long> lectureIds) {
        if (lectureIds == null || lectureIds.isEmpty()) {
            log.debug("Neo4jService: lectureIds empty → plannedHours = 0");
            return 0;
        }

        var result = client.query(
                        """
                        MATCH (g:Group {id: $groupId})-[r:HAS_SCHEDULE]->(l:Lecture)
                          WHERE l.id IN $ids
                        RETURN COUNT(r) * 2 AS hours
                        """
                )
                .bind(groupId).to("groupId")
                .bind(lectureIds).to("ids")
                .fetch().one()
                .map(rec -> ((Number) rec.get("hours")).intValue())
                .orElse(0);

        log.debug("Neo4jService: plannedHours for group {} = {}", groupId, result);
        return result;
    }
}
