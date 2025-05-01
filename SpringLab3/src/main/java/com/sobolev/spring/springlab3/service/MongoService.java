package com.sobolev.spring.springlab3.service;

import com.sobolev.spring.springlab3.dto.ReportDTO;
import com.sobolev.spring.springlab3.entity.UniversityDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MongoService {

    private final MongoTemplate mongoTemplate;

    public void enrichHierarchy(ReportDTO dto, Long deptId) {
        List<UniversityDocument> unis = mongoTemplate.findAll(UniversityDocument.class);
        for (var uni : unis) {
            for (var inst : uni.getInstitutes()) {
                for (var dept : inst.getDepartments()) {
                    if (dept.getDepartmentId().equals(deptId.intValue())) {
                        dto.setUniversity(uni.getName());
                        dto.setInstitute(inst.getName());
                        dto.setDepartment(dept.getName());
                        return;
                    }
                }
            }
        }
    }
}
