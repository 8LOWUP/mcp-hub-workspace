package com.mcphub.global.token.repository.redis.impl;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import com.mcphub.global.token.repository.redis.RedisRepository;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.*;

@Repository
@RequiredArgsConstructor
public class RedisRepositoryImpl implements RedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    private static final long REFRESH_EXPIRATION_TIME = 60 * 60 * 24 * 14;

    // refresh token을 key로 저장 (rotation 시 유리)
    @Override
    public void save(Long memberId, String refreshToken) {
        redisTemplate.opsForValue().set("refresh:" + refreshToken, memberId.toString(), REFRESH_EXPIRATION_TIME, TimeUnit.SECONDS);
    }

    @Override
    public Optional<Long> findMemberIdByToken(String refreshToken) {
        String memberId = redisTemplate.opsForValue().get("refresh:" + refreshToken);
        return Optional.ofNullable(memberId).map(Long::valueOf);
    }

    @Override
    public Boolean delete(String refreshToken) {
        return redisTemplate.delete("refresh:" + refreshToken);
    }

    @Override
    public Boolean blockAccessToken(String accessToken, Claims claims) {
        // TTL = 토큰 만료 시각 - 현재 시각
        Date expiration = claims.getExpiration();
        long ttl = expiration.getTime() - System.currentTimeMillis();

        if (ttl > 0) {
            // key = 토큰 문자열 그대로, value = 상태값 (blacklisted 라는 값은 그냥 value 채우기 용)
            redisTemplate.opsForValue().set("blacklist_access_token: " + accessToken, "blacklisted", ttl, TimeUnit.MILLISECONDS);
        }

        return true;
    }

    @Override
    public Boolean isTokenBlocked(String accessToken) {
        return redisTemplate.hasKey("blacklist_access_token: " + accessToken);
    }
}
