package com.mcphub.domain.workspace.llm.chatSender;

import com.mcphub.domain.workspace.dto.McpUrlTokenPair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class ClaudeChatSender implements ChatSender{
    @Override
    public String getResponse(String llmToken, List<McpUrlTokenPair> mcpUrlTokenList, String chatMessage) {

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

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
        for (McpUrlTokenPair mcpUrlTokenPair : mcpUrlTokenList) {
            JSONObject mcp_server = new JSONObject();
            mcp_server.put("type", "url");
            mcp_server.put("url", mcpUrlTokenPair.url());
            mcp_server.put("name", "mcp");
            mcp_server.put("authorization_token", mcpUrlTokenPair.token());

            mcp_servers.add(mcp_server);
        }
        requestBody.put("mcp_servers", mcp_servers);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.anthropic.com/v1/messages"))
                .header("anthropic-version", "2023-06-01")
                .header("x-api-key", "Bearer " + llmToken)
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toJSONString()))
                .build();

        try {
            // 요청 보내고 응답 받기
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();

        } catch (IOException | InterruptedException e) {
            return null;
        }
    }
}
