package com.mcphub.domain.workspace.adviser;

import com.mcphub.domain.workspace.common.McpInfo;
import com.mcphub.domain.workspace.converter.UserMcpConverter;
import com.mcphub.domain.workspace.dto.McpId;
import com.mcphub.domain.workspace.dto.McpUrlTokenPair;
import com.mcphub.domain.workspace.dto.event.McpSaveEvent;
import com.mcphub.domain.workspace.entity.McpUrl;
import com.mcphub.domain.workspace.entity.UserMcp;
import com.mcphub.domain.workspace.service.UserMcpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserMcpAdviser {
    private final UserMcpService userMcpService;
    private final UserMcpConverter userMcpConverter;

    public List<McpUrlTokenPair> getMcpUrlTokenPairList(String userId, List<McpInfo> mcpInfo) {
        List<UserMcp> userMcpList = userMcpService.getUserMcpListByMcpInfoList(userId, mcpInfo);

        List<McpId> mcpIdList = userMcpConverter.toMcpIdList(userMcpList);
        List<McpUrl> mcpUrlList = userMcpService.getMcpUrlListByMcpIdList(mcpIdList);

        //McpUrl, UserMcp로 McpUrlTokenPair 생성
        return userMcpConverter.toMcpUrlTokenPariList(userMcpList, mcpUrlList);
    }

    public void createUserMcp(McpSaveEvent mcpSaveEvent) {
        userMcpService.createUserMcp(mcpSaveEvent);
    }

    public void deleteUserMcp(McpSaveEvent mcpSaveEvent) {
        userMcpService.deleteUserMcp(mcpSaveEvent);
    }
}
