package com.mcphub.domain.workspace.adviser;

import com.mcphub.domain.workspace.common.McpInfo;
import com.mcphub.domain.workspace.converter.UserMcpConverter;
import com.mcphub.domain.workspace.dto.request.McpIdRequest;
import com.mcphub.domain.workspace.dto.response.McpUrlResponse;
import com.mcphub.domain.workspace.dto.McpUrlTokenPair;
import com.mcphub.domain.workspace.entity.UserMcp;
import com.mcphub.domain.workspace.service.UserMcpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMcpAdviser {
    private final UserMcpService userMcpService;
    private final UserMcpConverter userMcpConverter;

    public List<McpUrlTokenPair> getMcpUrlTokenPairList(String userId, List<McpInfo> mcpInfo) {
        List<UserMcp> userMcpList = userMcpService.getUserMcpListByMcpInfoList(userId, mcpInfo);
        List<McpIdRequest> mcpIdRequestList = userMcpConverter.toMcpIdList(userMcpList);
        List<McpUrlResponse> mcpUrlResponseList = userMcpService.getMcpUrlList(mcpIdRequestList);
        return null;
    }
}
