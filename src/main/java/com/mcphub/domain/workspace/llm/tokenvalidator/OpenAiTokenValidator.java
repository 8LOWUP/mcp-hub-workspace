package com.mcphub.domain.workspace.llm.tokenvalidator;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;

class OpenAiTokenValidator implements TokenValidator {
    @Override
    public boolean isInvalid(String token) {
        try {
            OpenAIClient client = OpenAIOkHttpClient.builder()
                    .apiKey(token)
                    .build();
            client.models().list();
        } catch(Exception e) {
            return true;
        }

        return false;
    }
}
