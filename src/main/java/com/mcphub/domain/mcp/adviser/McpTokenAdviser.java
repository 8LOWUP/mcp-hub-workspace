package com.mcphub.domain.mcp.adviser;

import com.mcphub.domain.mcp.dto.request.McpTokenRequest;
import com.mcphub.domain.mcp.service.mcpToken.McpTokenService;
import com.mcphub.global.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class McpTokenAdviser {

	private final McpTokenService mcpTokenService;
	private final SecurityUtils securityUtils;

	public Long saveMcpToken(Long mcpId, McpTokenRequest request) {
		Long userId = securityUtils.getUserId();
		return mcpTokenService.saveMcpToken(userId, mcpId, request);
	}

	public Long updateMcpToken(Long mcpId, McpTokenRequest request) {
		Long userId = securityUtils.getUserId();
		return mcpTokenService.updateMcpToken(userId, mcpId, request);
	}
}
