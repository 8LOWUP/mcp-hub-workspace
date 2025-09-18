package com.mcphub.domain.workspace.llm.chatSender;

import com.mcphub.domain.workspace.dto.McpUrlTokenPair;

import java.util.List;

public interface ChatSender {
    String getResponse(String llmToken, List<McpUrlTokenPair> mcpUrlTokenList, String chatMessage);
}
