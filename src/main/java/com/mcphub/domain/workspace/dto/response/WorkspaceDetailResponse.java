package com.mcphub.domain.workspace.dto.response;

import com.mcphub.domain.workspace.common.McpInfo;

import java.util.List;

public record WorkspaceDetailResponse(
        String workspaceId,
        String llmId,
        String userId,
        String title,
        List<McpInfo> mcps,
        List<Object> chats // TODO: 채팅 목록 가져올 객체 타입으로 변환 필요
) {
}
