package com.sobolev.spring.springlab1.service;

import com.sobolev.spring.springlab1.dto.MongoGroupInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MongoUniversityService {

    private final MongoTemplate mongoTemplate;

    public MongoGroupInfo getHierarchyByDepartmentId(Long departmentId) {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.unwind("institutes"),
                Aggregation.unwind("institutes.departments"),
                Aggregation.match(Criteria.where("institutes.departments.id").is(departmentId)),
                Aggregation.project()
                        .and("name").as("university")
                        .and("institutes.name").as("institute")
                        .and("institutes.departments.name").as("department")
        );

        AggregationResults<Document> result = mongoTemplate.aggregate(agg, "universities", Document.class);
        Document doc = result.getUniqueMappedResult();

        if (doc == null) {
            throw new RuntimeException("Кафедра с ID %s не найдена в MongoDB.".formatted(departmentId));
        }

        return MongoGroupInfo.builder()
                .university(doc.getString("university"))
                .institute(doc.getString("institute"))
                .department(doc.getString("department"))
                .build();
    }
}
