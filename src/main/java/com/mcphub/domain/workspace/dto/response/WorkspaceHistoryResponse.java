package com.mcphub.domain.workspace.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record WorkspaceHistoryResponse(
        String title,
        String workspaceId,
        LocalDateTime createdAt
) {
}
