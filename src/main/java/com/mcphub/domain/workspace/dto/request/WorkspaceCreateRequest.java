package com.mcphub.domain.workspace.dto.request;

import com.mcphub.domain.workspace.common.McpInfo;
import com.mcphub.domain.workspace.entity.enums.Llm;
import lombok.Builder;

import java.util.List;

@Builder
public record WorkspaceCreateRequest(
        Llm llmId,
        List<McpInfo> mcps,
        String chatMessage
) {
}
