package com.namiq.msuser.service;


import com.namiq.msuser.dao.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    @Value("${jwt.secret-key}") // SpEL - Spring Expression Language
    private String secretKey;

    @Getter
    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Getter
    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    public String generateAccessToken(User userEntity) {
        return Jwts.builder()
                .subject(userEntity.getUsername())
                .claim("role", userEntity.getRole().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + (accessTokenExpiration * 60 * 1000)))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("type", "refresh")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + (refreshTokenExpiration * 60 * 1000)))
                .signWith(getSigningKey())
                .compact();
    }

    public boolean isRefreshToken(String token) {
        return "refresh".equals(extractAllClaims(token).get("type", String.class));
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public List<String> extractRoles(String token) {
        String role = extractAllClaims(token).get("role", String.class);
        return List.of(role);
    }

    public boolean isTokenValid(String token) {
        try {
            return extractAllClaims(token)
                    .getExpiration()
                    .after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
}