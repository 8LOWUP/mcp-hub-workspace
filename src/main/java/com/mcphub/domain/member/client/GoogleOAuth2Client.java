package com.mcphub.domain.member.client;

import com.mcphub.domain.member.client.properties.GoogleOAuth2Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.mcphub.domain.member.dto.response.readmodel.GoogleProfile;

@Component
@RequiredArgsConstructor
public class GoogleOAuth2Client {

    private final WebClient webClient = WebClient.create();
    private final GoogleOAuth2Properties googleOAuth2Properties;

    public GoogleProfile getProfile(String code) {
        String accessToken = webClient.post()
                .uri("https://oauth2.googleapis.com/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", googleOAuth2Properties.getGrantType())
                        .with("client_id", googleOAuth2Properties.getClientId())
                        .with("client_secret", googleOAuth2Properties.getClientSecret())
                        .with("redirect_uri", googleOAuth2Properties.getRedirectUri())
                        .with("code", code))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block()
                .get("access_token").asText();

        return webClient.get()
                .uri("https://www.googleapis.com/oauth2/v2/userinfo")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(GoogleProfile.class)
                .block();
    }
}

