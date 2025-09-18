package com.mcphub.global.util;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiClient {
    private final RestTemplate restTemplate = new RestTemplate();

    public String callApiWithToken(String url, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken); // Bearer 토큰 형식으로 설정

        // HttpEntity에 헤더와 바디(요청 본문) 포함
        HttpEntity<String> entity = new HttpEntity<>(headers); // GET 요청은 바디가 필요 없으므로 null 또는 빈 문자열

        // exchange 메서드를 사용하여 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return response.getBody();
    }
}
