package com.mcphub.domain.workspace.llm.chatSender;

import com.mcphub.domain.workspace.dto.McpUrlTokenPair;
import com.mcphub.domain.workspace.entity.enums.Llm;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChatSenderManager {
    private final Map<Llm, ChatSender> validatorMap = Map.of(
            Llm.GEMINI, new GeminiChatSender(),
            Llm.GPT, new GptChatSender(),
            Llm.CLAUDE, new ClaudeChatSender()
    );

    public String getResponse(Llm llmId, String llmToken, List<McpUrlTokenPair> mcpUrlTokenList, String chatMessage) {
        return validatorMap.getOrDefault(llmId, new DefaultChatSender()).getResponse(llmToken, mcpUrlTokenList, chatMessage);
    }
}
