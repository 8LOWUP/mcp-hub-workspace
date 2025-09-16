package com.mcphub.domain.workspace.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record WorkspaceUpdateResponse(
        String workspaceId,
        String title,
        LocalDateTime updatedAt
) {
}
