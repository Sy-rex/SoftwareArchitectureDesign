package com.sobolev.spring.springlab3.service;

import com.sobolev.spring.springlab3.entity.GroupNode;
import com.sobolev.spring.springlab3.entity.LectureNode;
import com.sobolev.spring.springlab3.entity.StudentNode;
import com.sobolev.spring.springlab3.repository.GroupNodeRepository;
import com.sobolev.spring.springlab3.repository.LectureNodeRepository;
import com.sobolev.spring.springlab3.repository.StudentNodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class Neo4jService {
    private final GroupNodeRepository groupNodeRepository;
    private final StudentNodeRepository studentNodeRepository;
    private final LectureNodeRepository lectureNodeRepository;

    public List<Long> findSpecialLectureIdsByGroupId(Long groupId) {
        return groupNodeRepository.findSpecialLectureIdsByGroupId(groupId);
    }

    public Optional<GroupNode> findGroupNodeById(Long groupId) {
        return groupNodeRepository.findById(groupId);
    }

    public List<StudentNode> findStudentsByGroupId(Long groupId) {
        return studentNodeRepository.findByGroupId(groupId);
    }

    public Optional<LectureNode> findLectureById(Long lectureId) {
        return lectureNodeRepository.findById(lectureId);
    }
}
