package com.mcphub.domain.workspace.dto.response;

import lombok.Builder;

@Builder
public record McpUrlResponse(
        String mcpId,
        String mcpUrl
) {
}
