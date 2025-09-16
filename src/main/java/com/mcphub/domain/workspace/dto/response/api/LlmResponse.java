package com.mcphub.domain.workspace.dto.response.api;

import com.mcphub.domain.workspace.entity.enums.Llm;

public record LlmResponse(
        Llm llmId,
        String modelName,
        String llmProvider
) {
}
