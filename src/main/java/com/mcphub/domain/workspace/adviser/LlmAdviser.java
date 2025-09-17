package com.mcphub.domain.workspace.adviser;

import com.mcphub.domain.workspace.converter.LlmConverter;
import com.mcphub.domain.workspace.dto.response.api.LlmResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LlmAdviser {
    private final LlmConverter llmConverter;

    public LlmResponse getLlmList() {
        return llmConverter.toLlmResponse();
    }
}
