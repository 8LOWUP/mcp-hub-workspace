package com.mcphub.domain.workspace.dto;

import lombok.Builder;

@Builder
public record McpUrlTokenPair(
        String url,
        String token
) {
}
