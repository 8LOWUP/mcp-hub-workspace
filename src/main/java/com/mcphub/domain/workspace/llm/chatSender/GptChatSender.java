package com.mcphub.domain.workspace.llm.chatSender;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcphub.domain.workspace.dto.McpUrlTokenPair;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;


import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
public class GptChatSender implements ChatSender {
    @Override
    public JsonNode getResponse(String llmToken, List<McpUrlTokenPair> mcpUrlTokenList, String chatMessage) {
        // HTTP 클라이언트 생성
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-5");

        JSONArray tools = new JSONArray();
        for (McpUrlTokenPair mcpUrlTokenPair : ListUtils.emptyIfNull(mcpUrlTokenList)) {
            JSONObject tool = new JSONObject();
            tool.put("type", "mcp");
            tool.put("server_label", "mcps");
            tool.put("server_url", mcpUrlTokenPair.url());
            if (!Objects.equals(mcpUrlTokenPair.token(), ""))
                tool.put("authorization", mcpUrlTokenPair.token());
            tool.put("require_approval", "never");

            tools.add(tool);
        }
        requestBody.put("tools", tools);
        requestBody.put("input", chatMessage);

        // HttpRequest 객체 생성
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/responses"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + llmToken)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toJSONString()))
                .build();

        try {
            // 요청 보내고 응답 받기
            HttpResponse<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).orTimeout(1000, TimeUnit.SECONDS).join();

            JsonNode rootNode = objectMapper.readTree(response.body());
            JsonNode outputArray = rootNode.get("output");
            JsonNode node = null;

            if (outputArray != null && outputArray.isArray() && !outputArray.isEmpty()) {
                // 마지막 요소 반환
                node = outputArray.get(outputArray.size() - 1);
                node = node.get("content").get(0).get("text");
            }
            return node;

        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
