package com.mcphub.domain.workspace.adviser;

import com.mcphub.domain.workspace.converter.WorkspaceConverter;
import com.mcphub.domain.workspace.dto.request.WorkspaceCreateRequest;
import com.mcphub.domain.workspace.dto.request.WorkspaceMcpUpdateRequest;
import com.mcphub.domain.workspace.dto.request.WorkspaceUpdateRequest;
import com.mcphub.domain.workspace.dto.response.WorkspaceCreateResponse;
import com.mcphub.domain.workspace.dto.response.WorkspaceDetailResponse;
import com.mcphub.domain.workspace.dto.response.WorkspaceHistoryResponse;
import com.mcphub.domain.workspace.dto.response.WorkspaceUpdateResponse;
import com.mcphub.domain.workspace.entity.Workspace;
import com.mcphub.domain.workspace.service.WorkspaceService;
import com.mcphub.global.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class WorkspaceAdviser {

    private final SecurityUtils securityUtils;

    private final WorkspaceService workspaceService;

    private final WorkspaceConverter workspaceConverter;

    public WorkspaceCreateResponse createWorkspace(WorkspaceCreateRequest request) {

        Long userId = securityUtils.getUserId(); // 토큰에서 userId 가져오기
        Workspace createdWorkspace = null;

        // TODO: ChatService의 채팅 메서드 호출해서, LLM 응답과 요약 제목 가져오기 (_id 이용해서 Chat에 저장되도록 하기)
        String workspaceName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); // TODO: default 제목
        String response = "응답에 문제가 발생했습니다. 다시 질문해주세요."; // TODO: default 응답

        // 가장 최근의 워크스페이스 조회
        Workspace recentWorkspace = workspaceService.findRecentWorkspaceByUserId(userId.toString());
        // 최초 워크스페이스 생성이라면, request 로부터 설정값을 받아서 생성
        // 기존에 워크스페이스가 존재했다면, 가장 최근의 워크스페이스 설정을 참조해서 생성
        return Objects.isNull(recentWorkspace) ?
                workspaceConverter.toWorkspaceCreateResponse(workspaceService.createWorkspace(request, userId.toString(), workspaceName), response)
                : workspaceConverter.toWorkspaceCreateResponse(workspaceService.createWorkspaceByRecentWorkspace(recentWorkspace, userId.toString(), workspaceName), response);
    }

    public List<WorkspaceHistoryResponse> getWorkspaceHistory() {

        Long userId = securityUtils.getUserId(); // 토큰에서 userId 가져오기
        return workspaceService.getWorkspaceHistory(userId.toString()).stream().map(workspaceConverter::toWorkspaceHistoryResponse).toList();
    }

    public WorkspaceDetailResponse getWorkspaceDetail(String workspaceId) {

        Long userId = securityUtils.getUserId(); // 토큰에서 userId 가져오기

        // TODO: 해당 워크스페이스의 채팅 목록 조회

        return workspaceConverter.toWorkspaceDetailResponse(workspaceService.getWorkspaceDetail(workspaceId, userId), null); // TODO: 채팅 목록 객체 넣기
    }

    public WorkspaceUpdateResponse updateWorkspaceName(String workspaceId, WorkspaceUpdateRequest request) {
        Long userId = securityUtils.getUserId(); // 토큰에서 userId 가져오기

        return workspaceConverter.toWorkspaceUpdateResponse(workspaceService.updateWorkspace(request, workspaceId, userId.toString()));
    }

    public boolean deleteWorkspace(String workspaceId) {
        Long userId = securityUtils.getUserId(); // 토큰에서 userId 가져오기

        return workspaceService.deleteWorkspace(workspaceId, userId.toString());
    }

    public boolean updateActivatedMcpsInWorkspace(String workspaceId, WorkspaceMcpUpdateRequest request) {
        Long userId = securityUtils.getUserId(); // 토큰에서 userId 가져오기

        return workspaceService.updateWorkspaceMcpActivation(request, workspaceId, userId.toString());
    }
}
