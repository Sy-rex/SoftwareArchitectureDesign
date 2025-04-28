package com.sobolev.spring.springlab3.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node("Department")
@Getter
@Setter
public class DepartmentNode {
    @Id
    @Property("neo_id")
    private String neoId;

    private Long id;

    private String name;
}
