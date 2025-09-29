package com.mcphub.domain.workspace.llm.chatSender;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.genai.Client;
import com.google.genai.types.*;
import com.mcphub.domain.workspace.dto.McpUrlTokenPair;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.client.transport.HttpClientStreamableHttpTransport;
import io.modelcontextprotocol.spec.McpClientTransport;
import io.modelcontextprotocol.spec.McpSchema;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class GeminiChatSender implements ChatSender{
    @Override
    public JsonNode getResponse(String llmToken, List<McpUrlTokenPair> mcpUrlTokenList, String chatMessage) {
        try {


            Client client = Client.builder().apiKey(llmToken).build();
            for (McpUrlTokenPair mcpUrlTokenPair : mcpUrlTokenList) {
                /*McpClient mcpClient = McpClient.sync(
                        HttpClientStreamableHttpTransport.builder(mcpUrlTokenPair.url())
                                .customizeRequest()
                                .build()
                )*/



                List<FunctionDeclaration> functionDeclarations = new ArrayList<>();
                HttpClientStreamableHttpTransport transport = HttpClientStreamableHttpTransport.builder(mcpUrlTokenPair.url())
                        .customizeRequest(builder -> builder.header("Authorization", "Bearer " + mcpUrlTokenPair.token()))
                        .build();
                McpSyncClient mcpSyncClient = McpClient.sync(transport)
                        .requestTimeout(Duration.ofSeconds(30))// 타임아웃 설정
                        .build();
                mcpSyncClient.initialize();

                for (McpSchema.Tool tool: mcpSyncClient.listTools().tools()) {
                    functionDeclarations.add(convertMcpToolToGeminiTool(tool));
                }
                GenerateContentConfig config = GenerateContentConfig.builder()
                        .temperature(0F)
                        .tools(Tool.builder().functionDeclarations(functionDeclarations))
                        .build();
                GenerateContentResponse response = client.models.generateContent("gemini-2.5-flash", chatMessage, config);

                ImmutableList<FunctionCall> functionCalls = response.functionCalls();
                if (functionCalls.isEmpty()) {
                    continue;
                } else {
                    FunctionCall functionCall = functionCalls.get(0);
                    McpSchema.CallToolRequest toolRequest = McpSchema.CallToolRequest.builder()
                            .name(String.valueOf(functionCall.name()))
                            .arguments(functionCall.args().get())
                            .progressToken(mcpUrlTokenPair.token())
                            .build();

                    McpSchema.CallToolResult result = mcpSyncClient.callTool(toolRequest);
                    ObjectMapper objectMapper = new ObjectMapper();
                    return objectMapper.readTree(result.content().toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    private FunctionDeclaration convertMcpToolToGeminiTool(McpSchema.Tool tool) {
        return FunctionDeclaration.builder()
                .name(tool.name())
                .description(tool.description())
                .parametersJsonSchema(tool.inputSchema())
                .responseJsonSchema(tool.outputSchema())
                .build();
    }
}
