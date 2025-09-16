package com.mcphub.domain.workspace.llm.tokenvalidator;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;

public class AnthropicTokenValidator implements TokenValidator{
    @Override
    public boolean isInvalid(String token) {
        try {
            AnthropicClient client = AnthropicOkHttpClient.builder()
                    .apiKey(token)
                    .build();
            client.models().list();
        } catch(Exception e) {
            return true;
        }

        return false;
    }
}
