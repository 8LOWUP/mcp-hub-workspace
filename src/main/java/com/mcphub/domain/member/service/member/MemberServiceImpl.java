package com.mcphub.domain.member.service.member;

import com.mcphub.domain.member.entity.Member;
import com.mcphub.domain.member.repository.querydsl.MemberDslRepository;
import com.mcphub.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.mcphub.domain.member.status.MemberErrorStatus.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberDslRepository memberDslRepository;

    @Override
    public void existById(Long memberId) {
        if (!memberDslRepository.existById(memberId)) throw new RestApiException(MEMBER_NOT_FOUND);
    }

    public Member findById(Long memberId) throws UsernameNotFoundException {
        return memberDslRepository.findByIdNotFetchLoginInfo(memberId)
                .orElseThrow(() -> new RestApiException(MEMBER_NOT_FOUND));
    }

    @Override
    public Member findByIdNotFetchLoginInfo(Long memberId) {
        return memberDslRepository.findByIdNotFetchLoginInfo(memberId)
                .orElseThrow(() -> new RestApiException(MEMBER_NOT_FOUND));
    }


}
