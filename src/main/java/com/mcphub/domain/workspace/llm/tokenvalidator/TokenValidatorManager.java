package com.mcphub.domain.workspace.llm.tokenvalidator;

import com.mcphub.domain.workspace.dto.request.CreateLlmTokenCommand;
import com.mcphub.domain.workspace.dto.request.LlmTokenRequest;
import com.mcphub.domain.workspace.dto.request.ValidateLlmTokenCommand;
import com.mcphub.domain.workspace.entity.enums.Llm;
import com.mcphub.domain.workspace.status.LlmErrorStatus;
import com.mcphub.global.common.exception.RestApiException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TokenValidatorManager {
    private final Map<Llm, TokenValidator> validatorMap = Map.of(
            Llm.GEMINI, new GoogleTokenValidator(),
            Llm.GPT, new OpenAiTokenValidator(),
            Llm.CLAUDE, new AnthropicTokenValidator()
    );

    public void validateToken(LlmTokenRequest request) {
        if (validatorMap.getOrDefault(request.llmId(), t -> true).isInvalid(request.llmToken()))
            throw new RestApiException(LlmErrorStatus.INVALID_LLM_TOKEN);
    }
}
