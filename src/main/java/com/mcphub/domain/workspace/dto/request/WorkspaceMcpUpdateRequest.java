package com.mcphub.domain.workspace.dto.request;

import com.mcphub.domain.workspace.common.McpInfo;

import java.util.List;

public record WorkspaceMcpUpdateRequest(
        List<McpInfo> mcps
) {
}
