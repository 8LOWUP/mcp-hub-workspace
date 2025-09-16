package com.mcphub.domain.workspace.dto.request;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record WorkspaceUpdateRequest(
        String title

) {
}