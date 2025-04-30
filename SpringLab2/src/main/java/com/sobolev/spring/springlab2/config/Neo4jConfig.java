package com.sobolev.spring.springlab2.config;

import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Neo4jConfig {

    @Bean
    public Driver neo4jDriver(
            @Value("${spring.neo4j.uri}") String uri) {
        return GraphDatabase.driver(uri);
    }
}
