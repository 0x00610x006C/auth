package com.scltools.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class Token
{
    @Getter
    private final String token;

    private Token(String token)
    {
        this.token = token;
    }

    public static Token of(Long userId, Long validityInMinutes, String secretKey)
    {
        Instant issueDate = Instant.now();
        return new Token(Jwts.builder()
                .claim("user_id", userId)
                .setIssuedAt(Date.from(issueDate))
                .setExpiration(Date.from(issueDate.plus(validityInMinutes, ChronoUnit.MINUTES)))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes(StandardCharsets.UTF_8))
                .compact());

    }

    public static Long from(String token, String secretKey)
    {
        return ((Claims) Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                .build()
                .parse(token)
                .getBody())
                .get("user_id", Long.class);
    }

    public static Token of(String token)
    {
        return new Token(token);
    }
}
