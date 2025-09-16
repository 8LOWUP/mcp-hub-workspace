package com.mcphub.domain.workspace.adviser;

import com.mcphub.domain.workspace.converter.LlmTokenConverter;
import com.mcphub.domain.workspace.dto.request.CreateLlmTokenCommand;
import com.mcphub.domain.workspace.dto.request.LlmTokenRequest;
import com.mcphub.domain.workspace.dto.request.UpdateLlmTokenCommand;
import com.mcphub.domain.workspace.dto.response.api.LlmTokenListResponse;
import com.mcphub.domain.workspace.dto.response.api.LlmTokenSaveResponse;
import com.mcphub.domain.workspace.entity.LlmToken;
import com.mcphub.domain.workspace.llm.tokenvalidator.TokenValidatorManager;
import com.mcphub.domain.workspace.mapper.LlmTokenMapper;
import com.mcphub.domain.workspace.service.LlmTokenService;
import com.mcphub.global.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LlmTokenAdviser {
    private final LlmTokenService llmTokenService;
    private final LlmTokenMapper llmTokenMapper;
    private final LlmTokenConverter llmTokenConverter;
    private final SecurityUtils securityUtils;
    private final TokenValidatorManager tokenValidatorManager;

    public LlmTokenListResponse getToken() {
        Long userId = securityUtils.getUserId();
        List<LlmToken> result = llmTokenService.get(userId.toString());
        return llmTokenConverter.toLlmTokenListResponse(result);
    }

    public LlmTokenSaveResponse registerToken(LlmTokenRequest request) {
        tokenValidatorManager.validateToken(request);

        Long userId = securityUtils.getUserId();
        CreateLlmTokenCommand cmd = llmTokenMapper.toCreateCommand(request, userId.toString());
        LlmToken llmToken = llmTokenService.create(cmd);
        return llmTokenConverter.toLlmTokenSaveResponse(llmToken);
    }

    public LlmTokenSaveResponse updateToken(LlmTokenRequest request)  {
        tokenValidatorManager.validateToken(request);

        Long userId = securityUtils.getUserId();
        UpdateLlmTokenCommand cmd = llmTokenMapper.toUpdateCommand(request, userId.toString());
        LlmToken llmToken = llmTokenService.update(cmd);
        return llmTokenConverter.toLlmTokenSaveResponse(llmToken);
    }
}
