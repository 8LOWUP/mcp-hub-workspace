package com.mcphub.domain.workspace.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record WorkspaceChatHistoryResponse(
        String chatMessage,
        boolean isRequest,
        LocalDateTime createdAt
) {
}
