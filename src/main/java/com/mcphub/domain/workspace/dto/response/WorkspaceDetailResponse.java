package com.mcphub.domain.workspace.dto.response;

import com.mcphub.domain.workspace.common.McpInfo;
import com.mcphub.domain.workspace.entity.enums.Llm;

import java.util.List;

public record WorkspaceDetailResponse(
        String workspaceId,
        Llm llmId,
        String userId,
        String title,
        List<McpInfo> mcps
) {
}
