package com.mcphub.domain.workspace.llm.chatSender;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ClaudeChatSender implements ChatSender{
    @Override
    public JsonNode getResponse(String llmToken, List<McpUrlTokenPair> mcpUrlTokenList, String chatMessage) {

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "claude-sonnet-4-20250514");
        requestBody.put("max_tokens", 1000);

        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", chatMessage);

        JSONArray messages = new JSONArray();
        messages.add(message);
        requestBody.put("messages", messages);

        JSONArray mcp_servers = new JSONArray();
        int index = 0;
        for (McpUrlTokenPair mcpUrlTokenPair : ListUtils.emptyIfNull(mcpUrlTokenList)) {
            JSONObject mcp_server = new JSONObject();
            mcp_server.put("type", "url");
            mcp_server.put("url", mcpUrlTokenPair.url());
            mcp_server.put("name", "mcp"+index++);
            mcp_server.put("authorization_token", mcpUrlTokenPair.token());

            mcp_servers.add(mcp_server);
        }
        requestBody.put("mcp_servers", mcp_servers);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.anthropic.com/v1/messages"))
                .header("anthropic-version", "2023-06-01")
                .header("anthropic-beta", "mcp-client-2025-04-04")
                .header("x-api-key", llmToken)
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toJSONString()))
                .build();

        try {
            // 요청 보내고 응답 받기
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 400) {
                return TextNode.valueOf("[SERVER ERROR] MCP 서버 접속에 오류가 발생했습니다. MCP url이나 MCP 토큰 값을 확인해주세요.");
            }

            JsonNode rootNode = objectMapper.readTree(response.body());
            JsonNode outputArray = rootNode.get("content");
            JsonNode node = null;

            if (outputArray != null && outputArray.isArray() && !outputArray.isEmpty()) {
                // 마지막 요소 반환
                node = outputArray.get(outputArray.size() - 1);
                node = node.get("text");
            }
            return node;

        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
