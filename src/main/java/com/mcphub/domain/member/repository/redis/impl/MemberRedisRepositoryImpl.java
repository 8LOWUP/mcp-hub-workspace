package com.mcphub.domain.member.repository.redis.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import com.mcphub.domain.member.repository.redis.MemberRedisRepository;

import java.util.Optional;
import java.util.concurrent.*;

@Repository
@RequiredArgsConstructor
public class MemberRedisRepositoryImpl implements MemberRedisRepository {

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
    public void delete(String refreshToken) {
        redisTemplate.delete("refresh:" + refreshToken);
    }
}
