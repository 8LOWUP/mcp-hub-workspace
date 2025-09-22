package com.mcphub.domain.workspace.llm.chatSender;

import com.fasterxml.jackson.databind.JsonNode;
import com.mcphub.domain.workspace.dto.McpUrlTokenPair;

import java.util.List;

public class DefaultChatSender implements ChatSender{
    @Override
    public JsonNode getResponse(String llmToken, List<McpUrlTokenPair> mcpUrlTokenList, String chatMessage) {
        return null;
    }
}
