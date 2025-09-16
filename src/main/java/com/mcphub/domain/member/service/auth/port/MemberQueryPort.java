package com.mcphub.domain.member.service.auth.port;

import java.util.Optional;

import com.mcphub.domain.member.dto.response.readmodel.MemberRM;

public interface MemberQueryPort {
    Optional<MemberRM> findByEmail(String email);
}
