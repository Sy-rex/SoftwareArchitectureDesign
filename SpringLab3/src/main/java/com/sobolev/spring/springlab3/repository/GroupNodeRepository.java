package com.sobolev.spring.springlab3.repository;

import com.sobolev.spring.springlab3.entity.GroupNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupNodeRepository extends Neo4jRepository<GroupNode, Long> {
    @Query("MATCH (g:Group {id: $groupId})-[:BELONGS_TO]->(dept:Department) " +
            "MATCH (g)-[:HAS_SCHEDULE]->(l:Lecture) " +
            "WHERE NOT EXISTS ((l)-[:ORIGINATES_FROM]->(dept)) " +
            "RETURN l.id")
    List<Long> findSpecialLectureIdsByGroupId(Long groupId);

    Optional<GroupNode> findById(Long id);
}
