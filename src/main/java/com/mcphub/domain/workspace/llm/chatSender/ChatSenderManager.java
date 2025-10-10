package com.mcphub.domain.workspace.llm.chatSender;

import com.fasterxml.jackson.databind.JsonNode;
import com.mcphub.domain.workspace.dto.McpUrlTokenPair;
import com.mcphub.domain.workspace.entity.enums.Llm;
import com.mcphub.domain.workspace.status.LlmErrorStatus;
import com.mcphub.domain.workspace.status.WorkspaceErrorStatus;
import com.mcphub.global.common.exception.RestApiException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChatSenderManager {
    private final Map<Llm, ChatSender> validatorMap = Map.of(
            Llm.GPT, new GptChatSender(),
            Llm.CLAUDE, new ClaudeChatSender()
    );

    public JsonNode getResponse(Llm llmId, String llmToken, List<McpUrlTokenPair> mcpUrlTokenList, String chatMessage) {
        if(llmToken == null) {
            throw new RestApiException(LlmErrorStatus.TOKEN_NOT_EXISTS);
        }
        JsonNode response = validatorMap.getOrDefault(llmId, new DefaultChatSender()).getResponse(llmToken, mcpUrlTokenList, chatMessage);
        if (response == null) {
            throw new RestApiException(WorkspaceErrorStatus.CHAT_REQUEST_FAILED);
        }
        return response;
    }
}
