package com.mcphub.domain.workspace.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Llm {
    GEMINI("Google"),
    GPT("OpenAI"),
    CLAUDE("Anthropic");

    private final String provider;
}
