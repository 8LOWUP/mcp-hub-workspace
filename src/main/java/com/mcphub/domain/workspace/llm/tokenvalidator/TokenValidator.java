package com.mcphub.domain.workspace.llm.tokenvalidator;

public interface TokenValidator {
    boolean isInvalid(String token);
}
