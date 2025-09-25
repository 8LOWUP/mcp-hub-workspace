package com.mcphub.domain.workspace.dto.response;

import lombok.Builder;

@Builder
public record UserMcpTokenGetResponse(
        String platformId,
        String token
) {
}
