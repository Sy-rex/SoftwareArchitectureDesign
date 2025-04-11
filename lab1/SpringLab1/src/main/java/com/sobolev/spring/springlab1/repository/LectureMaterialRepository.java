package com.sobolev.spring.springlab1.repository;

import com.sobolev.spring.springlab1.entity.LectureMaterial;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LectureMaterialRepository extends ElasticsearchRepository<LectureMaterial,Long> {
    List<LectureMaterial> findByDescriptionContaining(String term);
}
