package com.mcphub.domain.workspace.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.mcphub.domain.workspace.common.McpInfo;
import com.mcphub.domain.workspace.entity.enums.Llm;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record WorkspaceCreateResponse(
        String userId, // 워크스페이스를 생성한 사용자 아이디
        String workspaceId, // 워크스페이스 아이디
        Llm llmId, // llm 아이디
        List<McpInfo> mcps, // mcp 아이디 목록
        JsonNode chatResponse, // llm 답장 내용
        String title, // 워크스페이스 제목
        LocalDateTime createdAt // 워크스페이스 생성 일자
) {
}
