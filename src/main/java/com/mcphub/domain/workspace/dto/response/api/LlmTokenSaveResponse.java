package com.mcphub.domain.workspace.dto.response.api;

import com.mcphub.domain.workspace.entity.enums.Llm;
import lombok.Builder;

@Builder
public record LlmTokenSaveResponse (
    Llm llmId
) {
}
