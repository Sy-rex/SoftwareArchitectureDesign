package com.sobolev.spring.springlab3.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("Student")
@Getter
@Setter
public class StudentNode {
    @Id
    @Property("student_number")
    private String studentNumber;

    private String fullname;

    @Property("redis_key")
    private String redisKey;

    @Relationship(type = "BELONGS_TO", direction = Relationship.Direction.OUTGOING)
    private GroupNode group;
}
