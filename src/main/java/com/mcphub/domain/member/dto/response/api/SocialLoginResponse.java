package com.mcphub.domain.member.dto.response.api;

import com.mcphub.domain.member.dto.response.readmodel.MemberRM;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SocialLoginResponse {
    private String accessToken;
    private String refreshToken;
    private MemberRM member;

    // 재발급용 (member 없이)
    public SocialLoginResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}

