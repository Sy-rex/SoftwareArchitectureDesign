package com.sobolev.spring.springlab1.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RedisStudentInfo {
    private String fullname;
    private String email;
    private Long groupId;
    private String groupName;
}
