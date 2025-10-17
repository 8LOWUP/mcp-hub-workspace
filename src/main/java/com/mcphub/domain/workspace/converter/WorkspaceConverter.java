package com.mcphub.domain.workspace.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.mcphub.domain.workspace.dto.response.*;
import com.mcphub.domain.workspace.entity.Chat;
import com.mcphub.domain.workspace.entity.Workspace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class WorkspaceConverter {

    public WorkspaceCreateResponse toWorkspaceCreateResponse(Workspace workspace, JsonNode response) {
        return new WorkspaceCreateResponse(workspace.getUserId(), workspace.getId(), workspace.getLlmId(), workspace.getMcps(), response, workspace.getTitle(), workspace.getCreatedAt());
    }

    public WorkspaceHistoryResponse toWorkspaceHistoryResponse(Workspace workspace) {
        return new WorkspaceHistoryResponse(workspace.getTitle(), workspace.getId(), workspace.getCreatedAt());
    }

    public WorkspaceDetailResponse toWorkspaceDetailResponse(Workspace workspace) {
        return new WorkspaceDetailResponse(workspace.getId(), workspace.getLlmId(), workspace.getUserId(), workspace.getTitle(), workspace.getMcps());
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

    public Page<WorkspaceChatHistoryResponse> toWorkspaceChatHistoryResponse(Page<Chat> chats) {
        // 1. DTO로 변환 후 내부 정렬
        List<WorkspaceChatHistoryResponse> content = chats.getContent().stream()
                .sorted(Comparator.comparing(Chat::getCreatedAt))
                .map(chat -> new WorkspaceChatHistoryResponse(
                        chat.getChat(),
                        chat.isRequest(),
                        chat.getCreatedAt()
                ))
                .toList();

        // 2. PageImpl로 감싸서 Page 반환
        return new PageImpl<>(content, chats.getPageable(), chats.getTotalElements());
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

    public String toWorkspaceNameRequestMessage(String requestMessage, String responseMessage) {
        return "다음 두 개의 메시지를 보고 적절한 채팅방 이름을 정해줘. 답변은 채팅방 이름만" + "\n" + "사용자: " + requestMessage + "\n" + "응답: " +responseMessage;
    }
}
