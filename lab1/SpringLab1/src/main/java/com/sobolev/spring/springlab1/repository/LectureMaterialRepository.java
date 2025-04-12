package com.sobolev.spring.springlab1.repository;

import com.sobolev.spring.springlab1.entity.LectureMaterial;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface LectureMaterialRepository extends ElasticsearchRepository<LectureMaterial,Long> {
    List<LectureMaterial> findByDescriptionContaining(String term);

    @Query("{\"query\":{\"match\":{\"description\":\"?0\"}},\"_source\":[\"lectureId\"]}")
    List<Long> searchLectureIdsByTerm(String term);
}
