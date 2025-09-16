package com.mcphub.domain.workspace.dto.request;

import com.mcphub.domain.workspace.entity.enums.Llm;
import lombok.Builder;

@Builder
public record LlmTokenRequest(
    Llm llmId,
    String llmToken
){

}
