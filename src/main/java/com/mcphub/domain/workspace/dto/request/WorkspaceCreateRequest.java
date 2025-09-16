package com.mcphub.domain.workspace.dto.request;

import com.mcphub.domain.workspace.common.McpInfo;
import lombok.Builder;

import java.util.List;

@Builder
public record WorkspaceCreateRequest(
        String llmId,
        List<McpInfo> mcps,
        String chatRequest
) {
}
