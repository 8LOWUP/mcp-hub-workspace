package com.mcphub.domain.member.repository.querydsl;

import com.mcphub.domain.member.entity.Member;

import java.util.Optional;

public interface MemberDslRepository {
    boolean existById(Long memberId);

    Optional<Member> findByIdNotFetchLoginInfo(Long memberId);

}
