package com.mcphub.domain.workspace.service;

import com.mcphub.domain.workspace.dto.request.CreateLlmTokenCommand;
import com.mcphub.domain.workspace.dto.request.UpdateLlmTokenCommand;
import com.mcphub.domain.workspace.dto.response.api.LlmTokenListResponse;
import com.mcphub.domain.workspace.entity.LlmToken;

import java.util.List;

public interface LlmTokenService {
    List<LlmToken> get(String userId);
    LlmToken create(CreateLlmTokenCommand cmd);
    LlmToken update(UpdateLlmTokenCommand cmd);
}
