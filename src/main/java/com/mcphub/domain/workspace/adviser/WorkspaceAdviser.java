package com.mcphub.domain.workspace.adviser;

import com.fasterxml.jackson.databind.JsonNode;
import com.mcphub.domain.workspace.converter.WorkspaceConverter;
import com.mcphub.domain.workspace.dto.McpUrlTokenPair;
import com.mcphub.domain.workspace.dto.request.*;
import com.mcphub.domain.workspace.dto.response.*;
import com.mcphub.domain.workspace.dto.response.api.LlmTokenResponse;
import com.mcphub.domain.workspace.entity.Chat;
import com.mcphub.domain.workspace.entity.UserMcp;
import com.mcphub.domain.workspace.entity.Workspace;
import com.mcphub.domain.workspace.llm.chatSender.ChatSenderManager;
import com.mcphub.domain.workspace.mapper.WorkspaceMapper;
import com.mcphub.domain.workspace.service.WorkspaceService;
import com.mcphub.global.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class WorkspaceAdviser {

    private final SecurityUtils securityUtils;

    private final WorkspaceService workspaceService;

    private final WorkspaceConverter workspaceConverter;
    private final LlmTokenAdviser llmTokenAdviser;
    private final UserMcpAdviser userMcpAdviser;
    private final ChatSenderManager chatSenderManager;

    private final int DEFAULT_PREVIOUS_CHATS = 5;

    public WorkspaceCreateResponse createWorkspace(WorkspaceCreateRequest request) {
        String userId = securityUtils.getUserId().toString(); // 토큰에서 userId 가져오기
        Workspace createdWorkspace = workspaceService.createWorkspace(userId, request);

        // 채팅 요청
        workspaceService.createChat(createdWorkspace.getId(), request.chatMessage(), true);
        List<McpUrlTokenPair> mcpUrlTokenPairs = userMcpAdviser.getMcpUrlTokenPairList(userId, createdWorkspace.getMcps());
        LlmTokenResponse llmTokenDto = llmTokenAdviser.getToken(createdWorkspace.getLlmId());
        JsonNode chatResponse = chatSenderManager.getResponse(
                llmTokenDto.llmId(),
                llmTokenDto.llmToken(),
                mcpUrlTokenPairs,
                request.chatMessage());
        workspaceService.createChat(createdWorkspace.getId(), chatResponse.toString(), false);

        // LLM을 이용해 채팅방 제목 생성 & 업데이트
        String requestMessage = workspaceConverter.toWorkspaceNameRequestMessage(request.chatMessage(), chatResponse.toString());
        String workspaceName = chatSenderManager
                .getResponse(llmTokenDto.llmId(), llmTokenDto.llmToken(), null, requestMessage)
                .toString()
                .replace("\"", "");
        WorkspaceUpdateRequest updateRequest = WorkspaceUpdateRequest
                .builder()
                .title(workspaceName)
                .build();
        Workspace updatedWorkspace = workspaceService.updateWorkspace(
                updateRequest,
                createdWorkspace.getId(),
                userId);

        return workspaceConverter.toWorkspaceCreateResponse(updatedWorkspace, chatResponse);
    }

    public List<WorkspaceHistoryResponse> getWorkspaceHistory() {

        Long userId = securityUtils.getUserId(); // 토큰에서 userId 가져오기
        return workspaceService.getWorkspaceHistory(userId.toString()).stream().map(workspaceConverter::toWorkspaceHistoryResponse).toList();
    }

    public WorkspaceDetailResponse getWorkspaceDetail(String workspaceId) {

        Long userId = securityUtils.getUserId(); // 토큰에서 userId 가져오기

        List<Chat> chats = workspaceService.getAllChats(workspaceId);

        return workspaceConverter.toWorkspaceDetailResponse(workspaceService.getWorkspaceDetail(workspaceId, userId.toString()), chats); // TODO: 채팅 목록 객체 넣기
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

    public WorkspaceChatResponse sendChat(String workspaceId, WorkspaceChatRequest request) {
        //유저 요청 저장
        workspaceService.createChat(workspaceId, request.chatMessage(), true);

        //workspaceId와 userId로 유저가 활성화한 mcp 리스트와 토큰값 가져오기
        String userId = securityUtils.getUserId().toString();
        Workspace workspace = workspaceService.getWorkspaceDetail(workspaceId, userId);

        //userMcpLsit => mcp_id => 요청 => 받아서 mcpUrlTokenPairs
        List<McpUrlTokenPair> mcpUrlTokenPairs = userMcpAdviser.getMcpUrlTokenPairList(userId, workspace.getMcps());

        //userId와 llmId로 llm 토큰 가져오기
        LlmTokenResponse llmTokenDto = llmTokenAdviser.getToken(workspace.getLlmId());

        //이전 채팅 기록 가져오기
        Page<Chat> chatList = workspaceService.getChats(workspaceId, DEFAULT_PREVIOUS_CHATS);
        String prompt = workspaceConverter.toPrompt(chatList, request.chatMessage());

        //메시지와 mcpUrl, mcpToken 값과 llmToken 값으로 llm API에 요청
        JsonNode llmResponse = chatSenderManager.getResponse(
                llmTokenDto.llmId(),
                llmTokenDto.llmToken(),
                mcpUrlTokenPairs,
                prompt);

        //response 저장
        workspaceService.createChat(workspaceId, llmResponse.toString(), false);

        return workspaceConverter.toWorkspaceChatResponse(workspaceId, llmResponse);
    }
}
