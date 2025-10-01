package com.mcphub.domain.workspace.adviser;

import com.mcphub.domain.workspace.converter.LlmTokenConverter;
import com.mcphub.domain.workspace.dto.request.CreateLlmTokenCommand;
import com.mcphub.domain.workspace.dto.request.LlmTokenRequest;
import com.mcphub.domain.workspace.dto.request.UpdateLlmTokenCommand;
import com.mcphub.domain.workspace.dto.response.api.LlmTokenListResponse;
import com.mcphub.domain.workspace.dto.response.api.LlmTokenResponse;
import com.mcphub.domain.workspace.dto.response.api.LlmTokenSaveResponse;
import com.mcphub.domain.workspace.entity.LlmToken;
import com.mcphub.domain.workspace.entity.enums.Llm;
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

    public LlmTokenListResponse getAllToken() {
        Long userId = securityUtils.getUserId();
        List<LlmToken> result = llmTokenService.getAll(userId.toString());
        return llmTokenConverter.toLlmTokenListResponse(result);
    }

    public LlmTokenResponse getToken(Llm llmId) {
        Long userId = securityUtils.getUserId();
        LlmToken result = llmTokenService.get(userId.toString(), llmId);
        return llmTokenConverter.toLlmTokenResponse(result);
    }

    public LlmTokenSaveResponse registerToken(Llm llmId, LlmTokenRequest request) {
        tokenValidatorManager.validateToken(llmId, request);

        Long userId = securityUtils.getUserId();
        CreateLlmTokenCommand cmd = llmTokenMapper.toCreateCommand(llmId, request, userId.toString());
        LlmToken llmToken = llmTokenService.create(cmd);
        return llmTokenConverter.toLlmTokenSaveResponse(llmToken);
    }

    public LlmTokenSaveResponse updateToken(Llm llmId, LlmTokenRequest request)  {
        tokenValidatorManager.validateToken(llmId, request);

        Long userId = securityUtils.getUserId();
        UpdateLlmTokenCommand cmd = llmTokenMapper.toUpdateCommand(llmId, request, userId.toString());
        LlmToken llmToken = llmTokenService.update(cmd);
        return llmTokenConverter.toLlmTokenSaveResponse(llmToken);
    }
}
