package com.bodna.logic_hub.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.bodna.logic_hub.domain.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));

            return JWT.create()
                .withIssuer("logic-hub-api")
                .withSubject(user.getEmail())
                .withExpiresAt(getExpirationDate())
                .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error while generating token", e);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));

            return JWT.require(algorithm)
                .withIssuer("logic-hub-api")
                .build()
                .verify(token)
                .getSubject();
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("Invalid Token", e);
        }
    }

    private Instant getExpirationDate() {
        return LocalDateTime.now().plusDays(3).toInstant(ZoneOffset.of("-03:00"));
    }
}
