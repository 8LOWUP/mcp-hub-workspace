package com.mcphub.domain.workspace.converter;

import com.mcphub.domain.workspace.dto.response.api.LlmResponse;
import com.mcphub.domain.workspace.entity.enums.Llm;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class LlmConverter {
    public LlmResponse toLlmResponse() {
        List<LlmResponse.LlmDto> llmList = Arrays.stream(Llm.values())
                .map(llm -> new LlmResponse.LlmDto(llm, llm.getModelName(), llm.getProvider()))
                .toList();

        return LlmResponse.builder()
                .llmList(llmList)
                .build();
    }
}
