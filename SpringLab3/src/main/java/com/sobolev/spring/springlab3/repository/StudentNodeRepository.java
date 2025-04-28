package com.sobolev.spring.springlab3.repository;

import com.sobolev.spring.springlab3.entity.StudentNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentNodeRepository extends Neo4jRepository<StudentNode, String> {
    @Query("MATCH (s:Student)-[:BELONGS_TO]->(g:Group {id: $groupId}) RETURN s")
    List<StudentNode> findByGroupId(Long groupId);
}
