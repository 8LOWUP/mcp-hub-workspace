package com.mcphub.domain.member.repository.redis;

import java.util.Optional;

import com.mcphub.domain.member.entity.RefreshToken;

public interface MemberRedisRepository {
    void save(Long memberId, String refreshToken);

    Optional<Long> findMemberIdByToken(String refreshToken);

    public void delete(String refreshToken);
}
