package com.mcphub.domain.workspace.converter;

import com.mcphub.domain.workspace.dto.request.McpIdRequest;
import com.mcphub.domain.workspace.entity.UserMcp;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMcpConverter {
    public List<McpIdRequest> toMcpIdList(List<UserMcp> userMcpList) {
        return userMcpList.stream()
                .map(userMcp -> McpIdRequest.builder()
                        .mcpId(userMcp.getMcpId())
                        .build())
                .toList();
    }
}
