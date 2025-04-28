package com.sobolev.spring.springlab3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    public Map<String, String> getStudentDetails(String redisKey) {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(redisKey);
        Map<String, String> result = new HashMap<>();
        entries.forEach((k, v) -> result.put(k.toString(), v != null ? v.toString() : null));
        return result;
    }
}
