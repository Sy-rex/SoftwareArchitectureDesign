package com.sobolev.spring.springlab1.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MongoGroupInfo {
    private String university;
    private String institute;
    private String department;

    public static MongoGroupInfo unknown() {
        return MongoGroupInfo.builder()
                .university("UNKNOWN")
                .institute("UNKNOWN")
                .department("UNKNOWN")
                .build();
    }
}
