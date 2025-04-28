package com.sobolev.spring.springlab3.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Node("Group")
@Getter
@Setter
public class GroupNode {
    @Id
    private Long id;

    private String name;

    @Property("mongo_id")
    private String mongoId;

    @Relationship(type = "BELONGS_TO", direction = Relationship.Direction.OUTGOING)
    private DepartmentNode department;

    @Relationship(type = "HAS_SCHEDULE", direction = Relationship.Direction.OUTGOING)
    private List<LectureNode> lectures;
}
