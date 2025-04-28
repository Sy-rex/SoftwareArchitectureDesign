package com.sobolev.spring.springlab3.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("Lecture")
@Getter
@Setter
public class LectureNode {
    @Id
    private Long id;

    private String name;

    @Property("course_id")
    private Long courseId;

    @Relationship(type = "ORIGINATES_FROM", direction = Relationship.Direction.OUTGOING)
    private DepartmentNode department;
}
