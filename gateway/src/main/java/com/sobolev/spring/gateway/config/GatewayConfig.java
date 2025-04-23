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
                        .filters(f -> f.stripPrefix(1))
                        .uri("http://spring-boot-app:8080"))

                .route("attendance-route", r -> r.path("/api/attendance-report/**")
                        .filters(f -> f.stripPrefix(1)
                                .filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
                        .uri("http://spring-boot-app:8080"))

                .build();
    }
}
