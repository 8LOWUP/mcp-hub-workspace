package com.mcphub.domain.workspace.service;

import com.mcphub.domain.workspace.dto.request.CreateLlmTokenCommand;
import com.mcphub.domain.workspace.dto.request.UpdateLlmTokenCommand;
import com.mcphub.domain.workspace.entity.LlmToken;
import com.mcphub.domain.workspace.entity.enums.Llm;

import java.util.List;

public interface LlmTokenService {
    List<LlmToken> getAll(String userId);
    LlmToken get(String userId, Llm llmId);
    LlmToken create(CreateLlmTokenCommand cmd);
    LlmToken update(UpdateLlmTokenCommand cmd);
}
