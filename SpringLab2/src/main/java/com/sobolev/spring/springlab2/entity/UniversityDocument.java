package com.sobolev.spring.springlab2.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "universities")
@Data
public class UniversityDocument {

    @Id
    private ObjectId _id;

    @Field("id")
    private Integer universityId;

    private String name;
    private List<InstituteDocument> institutes;
}
