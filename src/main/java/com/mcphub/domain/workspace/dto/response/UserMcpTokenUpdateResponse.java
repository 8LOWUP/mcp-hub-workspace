package com.mcphub.domain.workspace.dto.response;

import lombok.Builder;

@Builder
public record UserMcpTokenUpdateResponse(
        String mcpId
) {
}
