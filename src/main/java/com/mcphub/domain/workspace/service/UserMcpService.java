package com.mcphub.domain.workspace.service;

import com.mcphub.domain.workspace.common.McpInfo;
import com.mcphub.domain.workspace.dto.McpId;
import com.mcphub.domain.workspace.dto.event.McpSaveEvent;
import com.mcphub.domain.workspace.dto.event.UrlSaveEvent;
import com.mcphub.domain.workspace.dto.request.UserMcpTokenUpdateRequest;
import com.mcphub.domain.workspace.entity.McpUrl;
import com.mcphub.domain.workspace.entity.UserMcp;

import java.util.List;

public interface UserMcpService {
    List<UserMcp> getUserMcpListByMcpInfoList(String userId, List<McpInfo> mcpInfoList);
    List<McpUrl> getMcpUrlListByMcpIdList(List<McpId> mcpIdList);
    UserMcp getUserMcpToken(String userId, String platformId);
    UserMcp updateUserMcpToken(String userId, String platformId, UserMcpTokenUpdateRequest userMcpTokenUpdateRequest);
    Boolean checkUserMcpToken(String userId, String mcpId);

    UserMcp createUserMcp(McpSaveEvent mcpSaveEvent);
    UserMcp deleteUserMcp(McpSaveEvent mcpSaveEvent);
    List<UserMcp> deleteUserMcpByMcpId(String mcpId);
    McpUrl createOrUpdateMcpUrl(UrlSaveEvent urlSaveEvent);
    McpUrl deleteMcpUrl(UrlSaveEvent urlSaveEvent);
}
