package com.sobolev.spring.springlab3.service;

import com.sobolev.spring.springlab3.dto.ReportDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    public void enrichStudentInfo(ReportDTO dto, String studentNumber) {
        Map<Object,Object> entries =
                redisTemplate.opsForHash().entries("student:" + studentNumber);
        dto.setStudentName((String)entries.get("fullname"));
        dto.setEmail((String)entries.get("email"));
    }
}
