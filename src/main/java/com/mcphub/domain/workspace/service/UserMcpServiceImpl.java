package com.mcphub.domain.workspace.service;

import com.mcphub.domain.workspace.common.McpInfo;
import com.mcphub.domain.workspace.dto.request.McpIdRequest;
import com.mcphub.domain.workspace.dto.response.McpUrlResponse;
import com.mcphub.domain.workspace.entity.UserMcp;
import com.mcphub.domain.workspace.repository.mongo.UserMcpMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMcpServiceImpl implements UserMcpService {
    private final UserMcpMongoRepository userMcpMongoRepository;
    private final RabbitTemplate rabbitTemplate;

    @Override
    @Transactional
    public List<UserMcp> getUserMcpListByMcpInfoList(String userId, List<McpInfo> mcpInfoList) {
        List<UserMcp> userMcpList = new ArrayList<>();
        for (McpInfo mcpInfo : mcpInfoList) {
            if(mcpInfo.isActive())
                userMcpList.add(userMcpMongoRepository.findByUserIdAndMcpId(userId, mcpInfo.getId()));
        }

        return userMcpList;
    }

    @Override
    public List<McpUrlResponse> getMcpUrlList(List<McpIdRequest> mcpIdRequestList) {
        rabbitTemplate.convertAndSend("mcp.queue", mcpIdRequestList);
        return List.of();
    }
}
