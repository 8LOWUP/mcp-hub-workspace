package com.mcphub.domain.member.service.member;

import com.mcphub.domain.member.entity.Member;

public interface MemberService {
    void existById(Long memberId);

    Member findById(Long memberId);

    Member findByIdNotFetchLoginInfo(Long memberId);
}
