package com.namiq.msuser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenStorageService {

    private static final String ACCESS_TOKEN_PREFIX = "access_token:";
    private final StringRedisTemplate redisTemplate;
    private final JwtService jwtService;

    public void storeAccessToken(String username, String token) {
        String key = ACCESS_TOKEN_PREFIX + username;
        redisTemplate.opsForValue().set(key, token, jwtService.getAccessTokenExpiration(), TimeUnit.MINUTES);
    }

    public boolean isAccessTokenValid(String username, String token) {
        String key = ACCESS_TOKEN_PREFIX + username;
        String storedToken = redisTemplate.opsForValue().get(key);
        return token.equals(storedToken);
    }


}