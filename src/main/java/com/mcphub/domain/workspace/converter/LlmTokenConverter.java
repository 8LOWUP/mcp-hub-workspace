package com.mcphub.domain.workspace.converter;

import com.mcphub.domain.workspace.dto.response.api.LlmTokenListResponse;
import com.mcphub.domain.workspace.dto.response.api.LlmTokenSaveResponse;
import com.mcphub.domain.workspace.entity.LlmToken;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LlmTokenConverter {
    public LlmTokenListResponse toLlmTokenListResponse(List<LlmToken> llmTokenPage) {
        List<LlmTokenListResponse.LlmTokenDto> dtoList = llmTokenPage.stream()
                .map(llmToken -> LlmTokenListResponse.LlmTokenDto.builder()
                        .llmId(llmToken.getLlmId())
                        .llmToken(llmToken.getToken())
                        .build())
                .collect(Collectors.toList());

        return LlmTokenListResponse.builder()
                .llmTokens(dtoList)
                .build();
    }

    public LlmTokenSaveResponse toLlmTokenSaveResponse(LlmToken llmToken) {
        return LlmTokenSaveResponse.builder()
                .llmId(llmToken.getLlmId())
                .build();
    }
}
