package com.mcphub.domain.workspace.service;

import com.mcphub.domain.workspace.common.McpInfo;
import com.mcphub.domain.workspace.dto.request.McpIdRequest;
import com.mcphub.domain.workspace.dto.response.McpUrlResponse;
import com.mcphub.domain.workspace.entity.UserMcp;

import java.util.List;

public interface UserMcpService {
    List<UserMcp> getUserMcpListByMcpInfoList(String userId, List<McpInfo> mcpInfoList);
    List<McpUrlResponse> getMcpUrlList(List<McpIdRequest> mcpIdRequestList);
}
