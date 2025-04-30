package com.sobolev.spring.springlab2.service;

import com.sobolev.spring.springlab2.entity.UniversityDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MongoService{

    private final MongoTemplate mongoTemplate;

    public UniversityDocument getUniversityByDepartmentId(Long departmentId) {
        return mongoTemplate.findOne(
                Query.query(Criteria.where("institutes.departments.departmentId").is(departmentId.intValue())),
                UniversityDocument.class);
    }
}
