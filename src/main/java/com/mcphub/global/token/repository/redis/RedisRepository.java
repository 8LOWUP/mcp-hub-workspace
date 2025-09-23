package com.mcphub.global.token.repository.redis;

import java.util.Optional;

import io.jsonwebtoken.Claims;

public interface RedisRepository {
    void save(Long memberId, String refreshToken);

    Optional<Long> findMemberIdByToken(String refreshToken);

    Boolean delete(String refreshToken);

    Boolean blockAccessToken(String accessToken, Claims claims);

    Boolean isTokenBlocked(String accessToken);
}
