package com.mcphub.domain.workspace.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Llm {
    GPT("gpt-5", "OpenAI"),
    CLAUDE("claude-sonnet-4-20250514", "Anthropic");

    private final String modelName;
    private final String provider;
}
