package com.mcphub.domain.member.service.auth.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mcphub.domain.member.dto.response.readmodel.MemberRM;
import com.mcphub.domain.member.entity.Member;
import com.mcphub.domain.member.repository.Jpa.MemberRepository;
import com.mcphub.domain.member.repository.redis.MemberRedisRepository;
import com.mcphub.domain.member.service.auth.port.MemberCommandPort;
import com.mcphub.domain.member.service.auth.port.MemberQueryPort;
import com.mcphub.domain.member.status.AuthErrorStatus;
import com.mcphub.global.common.exception.RestApiException;
import com.mcphub.global.config.security.jwt.JwtProvider;
import com.mcphub.global.config.security.jwt.TokenInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberAuthServiceImpl implements MemberCommandPort, MemberQueryPort {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final MemberRedisRepository redisRepository;

    @Override
    @Transactional
    public MemberRM saveOrUpdate(String email, String nickname) {
        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> memberRepository.save(
                        Member.builder()
                                .email(email)
                                .nickname(nickname)
                                .build()
                ));
        return new MemberRM(member.getId(), member.getEmail(), member.getNickname());
    }

    @Override
    public Optional<MemberRM> findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .map(m -> new MemberRM(m.getId(), m.getEmail(), m.getNickname()));
    }

    // 토큰 재발급
    @Override
    @Transactional
    public TokenInfo reissueAccessToken(String refreshToken) {
        // 1. refresh token 존재 여부 검증
        Long memberId = redisRepository.findMemberIdByToken(refreshToken)
                .orElseThrow(() -> new RestApiException(AuthErrorStatus.INVALID_REFRESH_TOKEN));

        // 2. 기존 refresh token 제거 (rotation)
        redisRepository.delete(refreshToken);

        // 3. 새로운 access & refresh token 발급
        TokenInfo tokenInfo = jwtProvider.generateToken(memberId.toString());

        // 4. 새 refresh token Redis 저장
        redisRepository.save(memberId, tokenInfo.refreshToken()); // 2주

        return tokenInfo;
    }

}

