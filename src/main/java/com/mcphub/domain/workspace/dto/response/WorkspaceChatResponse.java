package com.mcphub.domain.workspace.dto.response;

import lombok.Builder;

@Builder
public record WorkspaceChatResponse(
        String workspaceId,
        String llmResponse
) {
}
