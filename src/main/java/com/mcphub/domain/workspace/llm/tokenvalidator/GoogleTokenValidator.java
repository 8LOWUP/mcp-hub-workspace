package com.mcphub.domain.workspace.llm.tokenvalidator;

import com.google.genai.Client;
import com.google.genai.types.ListModelsConfig;

class GoogleTokenValidator implements TokenValidator {
    @Override
    public boolean isInvalid(String token) {
        try (Client client = Client.builder().apiKey(token).build()) {
            client.models.list(ListModelsConfig.builder().build());
        } catch (Exception e) {
            return true;
        }

        return false;
    }
}
