package com.mcphub.domain.workspace.converter;

import com.mcphub.domain.workspace.dto.McpUrlTokenPair;
import com.mcphub.domain.workspace.dto.response.*;
import com.mcphub.domain.workspace.dto.response.api.LlmTokenListResponse;
import com.mcphub.domain.workspace.entity.UserMcp;
import com.mcphub.domain.workspace.entity.Workspace;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WorkspaceConverter {

    public WorkspaceCreateResponse toWorkspaceCreateResponse(Workspace workspace, String response) {
        return new WorkspaceCreateResponse(workspace.getUserId(), workspace.getId().toString(), workspace.getLlmId(), workspace.getMcps(), response, workspace.getTitle(), workspace.getCreatedAt());
    }

    public WorkspaceHistoryResponse toWorkspaceHistoryResponse(Workspace workspace) {
        return new WorkspaceHistoryResponse(workspace.getTitle(), workspace.getId().toString(), workspace.getCreatedAt());
    }

    public WorkspaceDetailResponse toWorkspaceDetailResponse(Workspace workspace, List<Object> chats) {
        return new WorkspaceDetailResponse(workspace.getId().toString(), workspace.getLlmId(), workspace.getUserId(), workspace.getTitle(), workspace.getMcps(), chats);
    }

    public WorkspaceUpdateResponse toWorkspaceUpdateResponse(Workspace workspace) {
        return new WorkspaceUpdateResponse(workspace.getId().toString(), workspace.getTitle(), workspace.getUpdatedAt());
    }

    public WorkspaceChatResponse toWorkspaceChatResponse(String workspaceId, String llmResponse) {
        return WorkspaceChatResponse.builder()
                .workspaceId(workspaceId)
                .llmResponse(llmResponse)
                .build();
    }

    public List<McpUrlTokenPair> toMcpUrlTokenPariList(List<UserMcp> userMcpList) {
        return userMcpList.stream()
                .map(userMcp -> McpUrlTokenPair.builder()
                        .url(userMcp.getRequestUrl())
                        .token(userMcp.getMcpToken())
                        .build())
                .toList();
    }
}
