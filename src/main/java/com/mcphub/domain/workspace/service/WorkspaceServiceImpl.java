package com.mcphub.domain.workspace.service;

import com.mcphub.domain.workspace.dto.request.WorkspaceCreateRequest;
import com.mcphub.domain.workspace.dto.request.WorkspaceMcpUpdateRequest;
import com.mcphub.domain.workspace.dto.request.WorkspaceUpdateRequest;
import com.mcphub.domain.workspace.entity.Workspace;
import com.mcphub.domain.workspace.repository.mongo.WorkspaceMongoRepository;
import com.mcphub.domain.workspace.status.WorkspaceErrorStatus;
import com.mcphub.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService {

    public static final int MCP_TOLERANCE_NUMBER = 3; // 워크스페이스 당 허용된 MCP 개수

    private final WorkspaceMongoRepository workspaceMongoRepository;

    @Override
    @Transactional(readOnly = true)
    public Workspace findRecentWorkspaceByUserId(String userId) {
        return workspaceMongoRepository.findTopByUserIdAndDeletedAtOrderByCreatedAtDesc(userId, null).orElse(null);
    }

    @Override
    @Transactional
    public Workspace createWorkspace(WorkspaceCreateRequest request, String userId, String workspaceName) {

        // request 관련 에러 처리
        if (request.llmId().isEmpty()
                || request.mcps().isEmpty()
                || userId.isEmpty()
                || workspaceName.isEmpty())
            throw new RestApiException(WorkspaceErrorStatus.WORKSPACE_PARAMETER_ERROR);

        // MCP 개수가 허용치를 초과할 경우 Exception
        if (request.mcps().size() > MCP_TOLERANCE_NUMBER)
            throw new RestApiException(WorkspaceErrorStatus.MCP_NUMBER_TOLERANCE_EXCEEDED);

        return workspaceMongoRepository.save(Workspace.builder()
                .llmId(request.llmId())
                .userId(userId)
                .mcps(request.mcps())
                .title(workspaceName)
                .build());
    }

    @Override
    @Transactional
    public Workspace createWorkspaceByRecentWorkspace(Workspace recentWorkspace, String userId, String workspaceName) {
        // request 관련 에러 처리
        if (userId.isEmpty()) throw new RestApiException(WorkspaceErrorStatus.USER_ID_NOT_FOUND_IN_TOKEN);
        if (recentWorkspace.getLlmId().isEmpty()
                || recentWorkspace.getMcps().isEmpty()
                || workspaceName.isEmpty())
            throw new RestApiException(WorkspaceErrorStatus.WORKSPACE_PARAMETER_ERROR);

        return workspaceMongoRepository.save(Workspace.builder()
                .llmId(recentWorkspace.getLlmId())
                .userId(userId)
                .mcps(recentWorkspace.getMcps())
                .title(workspaceName).build());
    }

    @Override
    @Transactional
    public List<Workspace> getWorkspaceHistory(String userId) {

        if (userId.isEmpty()) throw new RestApiException(WorkspaceErrorStatus.USER_ID_NOT_FOUND_IN_TOKEN);
        return workspaceMongoRepository.findByUserIdAndDeletedAtOrderByCreatedAtDesc(userId, null);
    }

    @Override
    @Transactional
    public Workspace getWorkspaceDetail(String workspaceId, Long userId) {

        // 워크스페이스를 조회한다. 없으면 Exception
        Workspace workspace = workspaceMongoRepository.findById(workspaceId).orElseThrow(() -> new RestApiException(WorkspaceErrorStatus.WORKSPACE_NOT_FOUND));

        if (!workspace.getUserId().equals(userId.toString()))
            throw new RestApiException(WorkspaceErrorStatus.MISMATCH_WORKSPACE_AND_USER);
        if (workspace.isDeleted()) throw new RestApiException(WorkspaceErrorStatus.DELETED_WORKSPACE);

        return workspace;
    }

    @Override
    @Transactional
    public Workspace updateWorkspace(WorkspaceUpdateRequest request, String workspaceId, String userId) {

        // 업데이트 할 워크스페이스 조회
        Workspace updatedWorkspace = workspaceMongoRepository.findById(workspaceId).orElseThrow(() -> new RestApiException(WorkspaceErrorStatus.WORKSPACE_NOT_FOUND));

        // BAD_REQUEST 체크
        if (updatedWorkspace.isDeleted()) throw new RestApiException(WorkspaceErrorStatus.DELETED_WORKSPACE);
        if (!updatedWorkspace.getUserId().equals(userId))
            throw new RestApiException(WorkspaceErrorStatus.MISMATCH_WORKSPACE_AND_USER);

        // 워크스페이스 이름 수정
        updatedWorkspace.setTitle(request.title());

        // 수정된 워크스페이스 반환
        return workspaceMongoRepository.save(updatedWorkspace);
    }

    @Override
    @Transactional
    public boolean deleteWorkspace(String workspaceId, String userId) {
        Workspace deletedWorkspace = workspaceMongoRepository.findById(workspaceId).orElseThrow(() -> new RestApiException(WorkspaceErrorStatus.WORKSPACE_NOT_FOUND));
        if (deletedWorkspace.isDeleted()) throw new RestApiException(WorkspaceErrorStatus.DELETED_WORKSPACE);
        if (!deletedWorkspace.getUserId().equals(userId))
            throw new RestApiException(WorkspaceErrorStatus.DELETED_WORKSPACE);
        deletedWorkspace.delete();
        return workspaceMongoRepository.save(deletedWorkspace).isDeleted();
    }

    @Override
    @Transactional
    public boolean updateWorkspaceMcpActivation(WorkspaceMcpUpdateRequest request, String workspaceId, String userId) {
        Workspace updatedWorkspace = workspaceMongoRepository.findById(workspaceId).orElseThrow(() -> new RestApiException(WorkspaceErrorStatus.WORKSPACE_NOT_FOUND));
        if (updatedWorkspace.isDeleted()) throw new RestApiException(WorkspaceErrorStatus.DELETED_WORKSPACE);
        if (!updatedWorkspace.getUserId().equals(userId))
            throw new RestApiException(WorkspaceErrorStatus.DELETED_WORKSPACE);

        // 요청에서 넘어온 mcps 의 active 값으로 업데이트
        updatedWorkspace.getMcps().forEach(existingMcp ->
                request.mcps().stream()
                        .filter(reqMcp -> reqMcp.getId().equals(existingMcp.getId()))
                        .findFirst()
                        .ifPresent(reqMcp -> existingMcp.setActive(reqMcp.isActive()))
        );

        workspaceMongoRepository.save(updatedWorkspace);
        return true;
    }
}
