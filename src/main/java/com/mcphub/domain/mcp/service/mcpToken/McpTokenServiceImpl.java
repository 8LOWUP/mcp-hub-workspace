package com.mcphub.domain.mcp.service.mcpToken;

import com.mcphub.domain.mcp.dto.request.McpTokenRequest;
import com.mcphub.domain.mcp.entity.Mcp;
import com.mcphub.domain.mcp.entity.UserMcp;
import com.mcphub.domain.mcp.repository.jsp.McpRepository;
import com.mcphub.domain.mcp.repository.jsp.UserMcpRepository;
import com.mcphub.global.common.exception.RestApiException;
import com.mcphub.global.common.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class McpTokenServiceImpl implements McpTokenService {

	private final McpRepository mcpRepository;
	private final UserMcpRepository userMcpRepository;

	@Override
	@Transactional
	public Long saveMcpToken(Long userId, Long mcpId, McpTokenRequest request) {
		Mcp mcp = mcpRepository.findByIdAndDeletedAtIsNull(mcpId)
		                       .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));

		UserMcp userMcp = userMcpRepository.findByUserIdAndMcp(userId, mcp)
		                                   .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));

		userMcp.setMcpToken(request.getToken());
		return userMcpRepository.save(userMcp).getId();
	}

	@Override
	@Transactional
	public Long updateMcpToken(Long userId, Long mcpId, McpTokenRequest request) {
		Mcp mcp = mcpRepository.findByIdAndDeletedAtIsNull(mcpId)
		                       .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));

		UserMcp userMcp = userMcpRepository.findByUserIdAndMcp(userId, mcp)
		                                   .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));

		userMcp.setMcpToken(request.getToken());
		return userMcpRepository.save(userMcp).getId();
	}

	@Override
	@Transactional
	public Long deleteMcpToken(Long userId, Long mcpId) {
		Mcp mcp = mcpRepository.findByIdAndDeletedAtIsNull(mcpId)
		                       .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));

		UserMcp userMcp = userMcpRepository.findByUserIdAndMcp(userId, mcp)
		                                   .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));

		userMcp.setMcpToken(null);
		return userMcpRepository.save(userMcp).getId();
	}
}
