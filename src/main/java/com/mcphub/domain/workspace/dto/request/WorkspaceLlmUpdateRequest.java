package com.mcphub.domain.workspace.dto.request;

import com.mcphub.domain.workspace.entity.enums.Llm;

public record WorkspaceLlmUpdateRequest(
        Llm llmId
) {
}
