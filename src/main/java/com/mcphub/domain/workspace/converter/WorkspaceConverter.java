package com.mcphub.domain.workspace.converter;

import com.mcphub.domain.workspace.dto.response.WorkspaceCreateResponse;
import com.mcphub.domain.workspace.dto.response.WorkspaceDetailResponse;
import com.mcphub.domain.workspace.dto.response.WorkspaceHistoryResponse;
import com.mcphub.domain.workspace.dto.response.WorkspaceUpdateResponse;
import com.mcphub.domain.workspace.entity.Workspace;
import org.springframework.stereotype.Component;

import java.util.List;

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
}
