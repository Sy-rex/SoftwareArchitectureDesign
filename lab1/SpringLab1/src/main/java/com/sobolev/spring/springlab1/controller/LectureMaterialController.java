package com.sobolev.spring.springlab1.controller;

import com.sobolev.spring.springlab1.dto.LectureMaterialDTO;
import com.sobolev.spring.springlab1.service.LectureMaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LectureMaterialController {

    private final LectureMaterialService lectureMaterialService;

    @GetMapping("/search")
    public List<LectureMaterialDTO> searchMaterials(@RequestParam String term) {
        System.out.println(term);
        return lectureMaterialService.searchMaterials(term);
    }
}
