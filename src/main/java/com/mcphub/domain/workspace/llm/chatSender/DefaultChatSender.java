package com.mcphub.domain.workspace.llm.chatSender;

import com.mcphub.domain.workspace.dto.McpUrlTokenPair;

import java.util.List;

public class DefaultChatSender implements ChatSender{
    @Override
    public String getResponse(String llmToken, List<McpUrlTokenPair> mcpUrlTokenList, String chatMessage) {
        return "No sender found for the specified LLM.";
    }
}
