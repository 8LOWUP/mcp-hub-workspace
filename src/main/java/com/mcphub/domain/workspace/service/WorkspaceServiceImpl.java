package com.mcphub.domain.workspace.service;

import com.mcphub.domain.workspace.common.McpInfo;
import com.mcphub.domain.workspace.dto.request.WorkspaceCreateRequest;
import com.mcphub.domain.workspace.dto.request.WorkspaceMcpUpdateRequest;
import com.mcphub.domain.workspace.dto.request.WorkspaceUpdateRequest;
import com.mcphub.domain.workspace.entity.Chat;
import com.mcphub.domain.workspace.entity.UserMcp;
import com.mcphub.domain.workspace.entity.Workspace;
import com.mcphub.domain.workspace.entity.enums.Llm;
import com.mcphub.domain.workspace.repository.mongo.ChatMongoRepository;
import com.mcphub.domain.workspace.repository.mongo.UserMcpMongoRepository;
import com.mcphub.domain.workspace.repository.mongo.WorkspaceMongoRepository;
import com.mcphub.domain.workspace.status.WorkspaceErrorStatus;
import com.mcphub.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService {

    public static final int MCP_TOLERANCE_NUMBER = 3; // 워크스페이스 당 허용된 MCP 개수

    private final WorkspaceMongoRepository workspaceMongoRepository;
    private final ChatMongoRepository chatMongoRepository;

    @Override
    @Transactional(readOnly = true)
    public Workspace findRecentWorkspaceByUserId(String userId) {
        return workspaceMongoRepository.findTopByUserIdAndDeletedAtOrderByCreatedAtDesc(userId, null).orElse(null);
    }

    @Override
    @Transactional
    public Workspace createWorkspace(String userId, WorkspaceCreateRequest request) {
        // request 관련 에러 처리
        if (userId.isEmpty())
            throw new RestApiException(WorkspaceErrorStatus.WORKSPACE_PARAMETER_ERROR);

        Optional<Workspace> recentWorkspaceOpt = workspaceMongoRepository.findTopByUserIdOrderByCreatedAtDesc(userId);
        Llm llmId = null;
        List<McpInfo> mcps = null;

        if (recentWorkspaceOpt.isPresent()) {
            // 이전 워크스페이스가 존재하는 경우
            Workspace recentWorkspace = recentWorkspaceOpt.get();
            llmId = recentWorkspace.getLlmId();
            mcps = recentWorkspace.getMcps();
        } else {
            // 이전 워크스페이스가 존재하지 않는 경우
            if (request.llmId() == null || request.mcps().isEmpty())
                throw new RestApiException(WorkspaceErrorStatus.WORKSPACE_PARAMETER_ERROR);

            llmId = request.llmId();
            mcps = request.mcps();
        }

        return workspaceMongoRepository.save(Workspace.builder()
                .llmId(llmId)
                .userId(userId)
                .mcps(mcps)
                .title(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build());
    }

    @Override
    @Transactional
    public List<Workspace> getWorkspaceHistory(String userId) {

        if (userId.isEmpty()) throw new RestApiException(WorkspaceErrorStatus.USER_ID_NOT_FOUND_IN_TOKEN);
        return workspaceMongoRepository.findByUserIdAndDeletedAtOrderByCreatedAtDesc(userId, null);
    }

    @Override
    @Transactional
    public Workspace getWorkspaceDetail(String workspaceId, String userId) {

        // 워크스페이스를 조회한다. 없으면 Exception
        Workspace workspace = workspaceMongoRepository.findById(workspaceId).orElseThrow(() -> new RestApiException(WorkspaceErrorStatus.WORKSPACE_NOT_FOUND));

        if (!workspace.getUserId().equals(userId))
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

    @Override
    @Transactional
    public Chat createChat(String workspaceId, String chatMessage, boolean isRequest) {
        if(!workspaceMongoRepository.existsById(workspaceId))
            throw new RestApiException(WorkspaceErrorStatus.WORKSPACE_NOT_FOUND);

        Chat chat = Chat.builder()
                .workspaceId(workspaceId)
                .chat(chatMessage)
                .isRequest(isRequest)
                .build();

        return chatMongoRepository.save(chat);
    }

    @Override
    @Transactional
    public List<Chat> getAllChats(String workspaceId) {
        return chatMongoRepository.findByWorkspaceId(workspaceId);
    }

    @Override
    @Transactional
    public Page<Chat> getChats(String workspaceId, int count) {
        return chatMongoRepository.findByWorkspaceIdOrderByCreatedAtDesc(workspaceId, PageRequest.of(0, count));
    }
}
