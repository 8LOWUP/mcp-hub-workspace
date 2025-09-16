package com.mcphub.domain.mcp.service.mcpToken;

import com.mcphub.domain.mcp.dto.request.McpTokenRequest;

public interface McpTokenService {
	Long saveMcpToken(Long userId, Long mcpId, McpTokenRequest request);

	Long updateMcpToken(Long userId, Long mcpId, McpTokenRequest request);

	Long deleteMcpToken(Long userId, Long mcpId);
}
