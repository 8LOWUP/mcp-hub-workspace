package com.mcphub.domain.workspace.service;

import com.mcphub.domain.workspace.common.McpInfo;
import com.mcphub.domain.workspace.dto.McpId;
import com.mcphub.domain.workspace.entity.McpUrl;
import com.mcphub.domain.workspace.entity.UserMcp;

import java.util.List;

public interface UserMcpService {
    List<UserMcp> getUserMcpListByMcpInfoList(String userId, List<McpInfo> mcpInfoList);
    List<McpUrl> getMcpUrlListByMcpIdList(List<McpId> mcpIdList);
}
