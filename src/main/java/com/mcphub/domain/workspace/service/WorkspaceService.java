package com.mcphub.domain.workspace.service;

import com.mcphub.domain.workspace.dto.request.WorkspaceCreateRequest;
import com.mcphub.domain.workspace.dto.request.WorkspaceMcpUpdateRequest;
import com.mcphub.domain.workspace.dto.request.WorkspaceUpdateRequest;
import com.mcphub.domain.workspace.entity.Workspace;

import java.util.List;

public interface WorkspaceService {

    Workspace findRecentWorkspaceByUserId(String userId); // 사용자와 연관된 가장 최근의 워크스페이스를 조회한다.

    Workspace createWorkspace(WorkspaceCreateRequest request, String userId, String workspaceName); // 워크스페이스를 생성한다.

    Workspace createWorkspaceByRecentWorkspace(Workspace recentWorkspace, String userId, String workspaceName);

    List<Workspace> getWorkspaceHistory(String userId);

    Workspace getWorkspaceDetail(String workspaceId, Long userId);

    Workspace updateWorkspace(WorkspaceUpdateRequest request, String workspaceId, String userId);

    boolean deleteWorkspace(String workspaceId, String userId);

    boolean updateWorkspaceMcpActivation(WorkspaceMcpUpdateRequest request, String workspaceId, String userId);
}
