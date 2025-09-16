package com.mcphub.domain.member.client.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.kakao")
@Getter
@Setter
public class KakaoOAuth2Properties {
    private String clientId;
    private String redirectUri;
    private String authorizationUri;
    private String tokenUri;
    private String userInfoUri;
}

