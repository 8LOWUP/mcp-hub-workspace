package com.mcphub.domain.member.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialLoginRequest {
    private String code;   // 카카오/구글에서 받은 인가 코드
}
