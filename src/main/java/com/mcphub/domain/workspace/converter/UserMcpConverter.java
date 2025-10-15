package com.mcphub.domain.workspace.converter;

import com.mcphub.domain.workspace.dto.McpId;
import com.mcphub.domain.workspace.dto.McpUrlTokenPair;
import com.mcphub.domain.workspace.dto.response.UserMcpTokenCheckResponse;
import com.mcphub.domain.workspace.dto.response.UserMcpTokenGetResponse;
import com.mcphub.domain.workspace.dto.response.UserMcpTokenUpdateResponse;
import com.mcphub.domain.workspace.entity.McpUrl;
import com.mcphub.domain.workspace.entity.UserMcp;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class UserMcpConverter {
    public List<McpId> toMcpIdList(List<UserMcp> userMcpList) {
        return userMcpList.stream()
                .map(userMcp -> McpId.builder()
                        .mcpId(userMcp.getId().getMcpId())
                        .build())
                .toList();
    }

    public List<McpUrlTokenPair> toMcpUrlTokenPariList(List<UserMcp> userMcpList, List<McpUrl> mcpUrlList) {
        // McpUrl 리스트를 Map으로 변환 (mcpId → McpUrl)
        Map<String, McpUrl> urlMap = mcpUrlList.stream()
                .collect(Collectors.toMap(McpUrl::getMcpId, Function.identity()));

        // UserMcp 기준으로 조인
        return userMcpList.stream()
                .map(userMcp -> {
                    McpUrl mcpUrl = urlMap.get(userMcp.getId().getMcpId());
                    if (mcpUrl != null) {
                        return new McpUrlTokenPair(mcpUrl.getMcpUrl(), userMcp.getMcpToken());
                    }
                    return null; // 매칭이 없을 경우
                })
                .filter(Objects::nonNull) // INNER JOIN 방식
                .collect(Collectors.toList());
    }

    public UserMcpTokenGetResponse toUserMcpTokenGetResponse(UserMcp userMcp) {
        return UserMcpTokenGetResponse.builder()
                .platformId(userMcp.getPlatformId())
                .token(userMcp.getMcpToken())
                .build();
    }

    public UserMcpTokenUpdateResponse toUserMcpTokenUpdateResponse(UserMcp userMcp) {
        return UserMcpTokenUpdateResponse.builder()
                .platformId(userMcp.getPlatformId())
                .build();
    }
}
