package com.sobolev.spring.springlab1.service;

import com.sobolev.spring.springlab1.dto.RedisStudentInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisStudentInfo getStudentInfo(String studentNumber) {
        String key = "student:" + studentNumber;
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);

        if (entries == null || entries.isEmpty()) {
            throw new RuntimeException("Student not found in Redis for key: " + key);
        }

        return RedisStudentInfo.builder()
                .fullname((String) entries.get("fullname"))
                .email((String) entries.get("email"))
                .groupId(Long.parseLong((String) entries.get("group_id")))
                .groupName((String) entries.get("group_name"))
                .build();
    }
}
