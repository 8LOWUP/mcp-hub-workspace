package com.mcphub.domain.workspace.service;

import com.mcphub.domain.workspace.common.McpInfo;
import com.mcphub.domain.workspace.dto.McpId;
import com.mcphub.domain.workspace.dto.event.McpSaveEvent;
import com.mcphub.domain.workspace.entity.McpUrl;
import com.mcphub.domain.workspace.entity.UserMcp;
import com.mcphub.domain.workspace.repository.mongo.McpUrlMongoRepository;
import com.mcphub.domain.workspace.repository.mongo.UserMcpMongoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserMcpServiceImpl implements UserMcpService {
    private final UserMcpMongoRepository userMcpMongoRepository;
    private final McpUrlMongoRepository mcpUrlMongoRepository;

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
    @Transactional
    public List<McpUrl> getMcpUrlListByMcpIdList(List<McpId> mcpIdList) {
        List<McpUrl> mcpUrlList = new ArrayList<>();
        for(McpId mcpId : mcpIdList){
            mcpUrlList.add(mcpUrlMongoRepository.findByMcpId(mcpId.mcpId()));
        }
        return mcpUrlList;
    }

    @Override
    @Transactional
    public UserMcp createUserMcp(McpSaveEvent mcpSaveEvent) {
        if(mcpSaveEvent.getMcpId() == null || mcpSaveEvent.getUserId() == null){

        }
        UserMcp userMcp = UserMcp.builder()
                .mcpId(mcpSaveEvent.getMcpId().toString())
                .userId(mcpSaveEvent.getUserId().toString())
                .build();
        return userMcpMongoRepository.save(userMcp);
    }

    @Override
    public UserMcp deleteUserMcp(McpSaveEvent mcpSaveEvent) {
        if(!userMcpMongoRepository.existsByMcpIdAndUserId(mcpSaveEvent.getMcpId().toString(), mcpSaveEvent.getUserId().toString())){

        }
        return userMcpMongoRepository.deleteByMcpIdAndUserId(mcpSaveEvent.getMcpId().toString(), mcpSaveEvent.getUserId().toString());
    }


}
