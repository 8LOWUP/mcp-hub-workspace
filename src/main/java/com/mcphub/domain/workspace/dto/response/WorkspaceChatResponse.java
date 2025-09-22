package com.mcphub.domain.workspace.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;

@Builder
public record WorkspaceChatResponse(
        String workspaceId,
        JsonNode llmResponse
) {
}
