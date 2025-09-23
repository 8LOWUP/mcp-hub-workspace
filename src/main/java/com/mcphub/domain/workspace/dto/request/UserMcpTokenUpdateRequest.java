package com.mcphub.domain.workspace.dto.request;

import lombok.Builder;

@Builder
public record UserMcpTokenUpdateRequest(
        String token
) {

}
