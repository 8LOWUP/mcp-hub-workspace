package com.mcphub.domain.workspace.mapper;

import com.mcphub.domain.workspace.dto.request.CreateLlmTokenCommand;
import com.mcphub.domain.workspace.dto.request.LlmTokenRequest;
import com.mcphub.domain.workspace.dto.request.UpdateLlmTokenCommand;
import com.mcphub.domain.workspace.dto.request.ValidateLlmTokenCommand;
import com.mcphub.domain.workspace.entity.LlmToken;
import com.mcphub.domain.workspace.entity.enums.Llm;
import org.springframework.stereotype.Component;

@Component
public class LlmTokenMapper {
    public CreateLlmTokenCommand toCreateCommand(Llm llmId, LlmTokenRequest request, String userId) {
        return CreateLlmTokenCommand.builder()
                .llmId(llmId)
                .llmToken(request.llmToken())
                .userId(userId)
                .build();
    }

    public UpdateLlmTokenCommand toUpdateCommand(Llm llmId, LlmTokenRequest request, String userId) {
        return UpdateLlmTokenCommand.builder()
                .llmId(llmId)
                .llmToken(request.llmToken())
                .userId(userId)
                .build();
    }
}
