package com.mcphub.domain.member.service.auth.port;

import com.mcphub.domain.member.dto.response.readmodel.MemberRM;
import com.mcphub.global.config.security.jwt.TokenInfo;

public interface MemberCommandPort {
    MemberRM saveOrUpdate(String email, String nickname);

    TokenInfo reissueAccessToken(String refreshToken);
}
