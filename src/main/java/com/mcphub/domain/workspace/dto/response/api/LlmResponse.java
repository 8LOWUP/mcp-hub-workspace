package com.mcphub.domain.workspace.dto.response.api;

import com.mcphub.domain.workspace.entity.enums.Llm;
import lombok.Builder;

import java.util.List;

@Builder
public record LlmResponse(
        List<LlmDto> llmList
) {
    @Builder
    public record LlmDto(
            Llm llmId,
            String modelName,
            String llmProvider
    ) {
    }
}
