package com.mcphub.domain.member.client.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.google")
@Getter
@Setter
public class GoogleOAuth2Properties {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String grantType;
}
