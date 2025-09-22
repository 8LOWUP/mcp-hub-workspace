package com.mcphub.domain.workspace.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcphub.domain.workspace.dto.McpUrlTokenPair;
import com.mcphub.domain.workspace.dto.response.*;
import com.mcphub.domain.workspace.dto.response.api.LlmTokenListResponse;
import com.mcphub.domain.workspace.entity.Chat;
import com.mcphub.domain.workspace.entity.UserMcp;
import com.mcphub.domain.workspace.entity.Workspace;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class WorkspaceConverter {

    public WorkspaceCreateResponse toWorkspaceCreateResponse(Workspace workspace, String response) {
        return new WorkspaceCreateResponse(workspace.getUserId(), workspace.getId(), workspace.getLlmId(), workspace.getMcps(), response, workspace.getTitle(), workspace.getCreatedAt());
    }

    public WorkspaceHistoryResponse toWorkspaceHistoryResponse(Workspace workspace) {
        return new WorkspaceHistoryResponse(workspace.getTitle(), workspace.getId(), workspace.getCreatedAt());
    }

    public WorkspaceDetailResponse toWorkspaceDetailResponse(Workspace workspace, List<Chat> chats) {
        List<Object> chatObjects = chats.stream()
                .map(chat -> (Object) chat)
                .toList();
        return new WorkspaceDetailResponse(workspace.getId(), workspace.getLlmId(), workspace.getUserId(), workspace.getTitle(), workspace.getMcps(), chatObjects);
    }

    public WorkspaceUpdateResponse toWorkspaceUpdateResponse(Workspace workspace) {
        return new WorkspaceUpdateResponse(workspace.getId(), workspace.getTitle(), workspace.getUpdatedAt());
    }

    public WorkspaceChatResponse toWorkspaceChatResponse(String workspaceId, JsonNode llmResponse) {
        return WorkspaceChatResponse.builder()
                .workspaceId(workspaceId)
                .llmResponse(llmResponse)
                .build();
    }

    public String toPrompt(Page<Chat> chats, String chatMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append("다음은 이전 대화 내용입니다:\n\n");

        for (Chat chat : chats.getContent()) {
            if (chat.isRequest()) {
                sb.append("사용자: ").append(chat.getChat()).append("\n");
            } else {
                sb.append("챗봇: ").append(chat.getChat()).append("\n");
            }
        }

        sb.append("\n이어서 답변해 주세요.\n");
        sb.append(chatMessage);
        return sb.toString();
    }
}
