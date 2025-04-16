package com.sobolev.spring.springlab1.service;

import com.sobolev.spring.springlab1.dto.MongoGroupInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MongoUniversityService {

    private final MongoTemplate mongoTemplate;

    public MongoGroupInfo getHierarchyByDepartmentId(Long departmentId) {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.unwind("institutes"),
                Aggregation.unwind("institutes.departments"),
                Aggregation.match(Criteria.where("institutes.departments.departmentId").is(departmentId)),
                Aggregation.group("name")
                        .first("name").as("university")
                        .first("institutes.name").as("institute")
                        .first("institutes.departments.name").as("department")
        ).withOptions(AggregationOptions.builder().allowDiskUse(true).build());


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