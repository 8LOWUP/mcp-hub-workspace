package com.mcphub.domain.member.dto.response.member.common;

import lombok.Builder;

@Builder
public record MemberIdResponse(
        Long memberId
) {

}
