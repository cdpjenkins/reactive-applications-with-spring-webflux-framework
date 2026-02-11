package com.cdpjenkins.users.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.core.env.Environment;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtServiceImpl implements JwtService {
    final private Environment environment;

    public JwtServiceImpl(Environment environment) {
        this.environment = environment;
    }

    @Override
    public String generateJwt(String subject) {

        return Jwts.builder()
                .subject(subject)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
                .signWith(getSigningKey())
                .compact();
    }

    @Override
    public Mono<Boolean> validateJwt(String token) {
        return Mono.just(token)
                .map(jwt -> parseToken(token))
                .map(claims -> !claims.getExpiration().before(Date.from(Instant.now())));
    }

    @Override
    public String extractTokenSubject(String token) {
        return parseToken(token)
                .getSubject();
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Nullable
    private SecretKey getSigningKey() {
        return Optional.ofNullable(environment.getProperty("token.secret"))
                .map(String::getBytes)
                .map(Keys::hmacShaKeyFor)
                .orElseThrow(() -> new IllegalArgumentException("token.secret must be configured for the application to work"));
    }
}
