package com.sobolev.spring.springlab1.service;

import com.sobolev.spring.springlab1.dto.LectureMaterialDTO;
import com.sobolev.spring.springlab1.entity.LectureMaterial;
import com.sobolev.spring.springlab1.repository.LectureMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LectureMaterialService {

    private final LectureMaterialRepository lectureMaterialRepository;

    public List<LectureMaterialDTO> searchMaterials(String term) {
        List<LectureMaterial> materials = lectureMaterialRepository.findByDescriptionContaining(term);
        return materials.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private LectureMaterialDTO convertToDto(LectureMaterial material) {
        LectureMaterialDTO dto = new LectureMaterialDTO();
        dto.setId(material.getId());
        dto.setName(material.getName());
        dto.setDescription(material.getDescription());
        dto.setLectureId(material.getLectureId());
        return dto;
    }
}
