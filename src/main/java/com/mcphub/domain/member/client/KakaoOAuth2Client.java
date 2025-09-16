package com.mcphub.domain.member.client;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.mcphub.domain.member.client.properties.KakaoOAuth2Properties;
import com.mcphub.domain.member.dto.response.readmodel.KakaoProfile;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KakaoOAuth2Client {
    private final WebClient webClient = WebClient.create();
    private final KakaoOAuth2Properties properties;

    public KakaoProfile getProfile(String code) {
        String accessToken = webClient.post()
                .uri(properties.getTokenUri())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", properties.getClientId())
                        .with("redirect_uri", properties.getRedirectUri())
                        .with("code", code))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block()
                .get("access_token").asText();

        return webClient.get()
                .uri(properties.getUserInfoUri())
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(KakaoProfile.class)
                .block();
    }
}


