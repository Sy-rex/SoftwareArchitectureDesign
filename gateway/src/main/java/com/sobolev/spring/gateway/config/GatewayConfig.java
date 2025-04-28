package com.sobolev.spring.gateway.config;

import com.sobolev.spring.gateway.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-route", r -> r.path("/api/auth/**")
                        .uri("http://spring-boot-app:8081"))

                .route("attendance-route", r -> r.path("/api/attendance-report/**")
                        .filters(f -> f
                                .filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
                        .uri("http://spring-boot-app:8081"))

                .route("reports", r -> r.path("/api/reports/**")
                        .filters(f -> f
                                .filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
                        .uri("http://spring-boot-app-2:8082"))

                .route("reports3", r -> r.path("/api/reports3/**")
                        .filters(f -> f
                                .filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
                        .uri("http://spring-boot-app-3:8083"))
                .build();
    }
}
