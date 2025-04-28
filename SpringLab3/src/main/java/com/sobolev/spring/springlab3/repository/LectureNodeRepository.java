package com.sobolev.spring.springlab3.repository;

import com.sobolev.spring.springlab3.entity.LectureNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface LectureNodeRepository extends Neo4jRepository<LectureNode, Long> {
}
