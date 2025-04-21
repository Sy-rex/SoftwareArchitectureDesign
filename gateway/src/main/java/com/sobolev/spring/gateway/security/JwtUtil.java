package com.sobolev.spring.gateway.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    @Value("${jwt_secret}")
    private String secret;

    public boolean validateToken(String token) {
        try {
            getVerifier().verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return JWT.decode(token).getSubject();
    }

    private JWTVerifier getVerifier() {
        return JWT.require(Algorithm.HMAC256(secret))
                .withIssuer("sobolev")
                .build();
    }
}
