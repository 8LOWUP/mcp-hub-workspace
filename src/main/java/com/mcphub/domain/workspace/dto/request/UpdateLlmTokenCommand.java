package com.mcphub.domain.workspace.dto.request;

import com.mcphub.domain.workspace.entity.enums.Llm;
import lombok.Builder;

@Builder
public record UpdateLlmTokenCommand(
        Llm llmId,
        String llmToken,
        String userId
) {
}
