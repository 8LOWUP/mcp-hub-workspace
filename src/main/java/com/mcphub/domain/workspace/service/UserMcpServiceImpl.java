package com.mcphub.domain.workspace.service;

import com.mcphub.domain.workspace.common.McpInfo;
import com.mcphub.domain.workspace.dto.McpId;
import com.mcphub.domain.workspace.dto.request.UserMcpTokenUpdateRequest;
import com.mcphub.domain.workspace.dto.response.UserMcpTokenCheckResponse;
import com.mcphub.domain.workspace.entity.McpUrl;
import com.mcphub.domain.workspace.entity.UserMcp;
import com.mcphub.domain.workspace.repository.mongo.McpUrlMongoRepository;
import com.mcphub.domain.workspace.repository.mongo.UserMcpMongoRepository;
import com.mcphub.domain.workspace.status.UserMcpErrorStatus;
import com.mcphub.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
    private final StringEncryptor stringEncryptor;
    private final MongoTemplate mongoTemplate;

    @Override
    @Transactional
    public List<UserMcp> getUserMcpListByMcpInfoList(String userId, List<McpInfo> mcpInfoList) {
        List<UserMcp> userMcpList = new ArrayList<>();
        for (McpInfo mcpInfo : mcpInfoList) {
            if(mcpInfo.isActive()) {
                UserMcp userMcp = userMcpMongoRepository.findByIdUserIdAndIdMcpId(userId, mcpInfo.getId()).orElseThrow(() -> new RestApiException(UserMcpErrorStatus.MCP_NOT_YET_REGISTERED_FOR_USER));
                if (userMcp.getMcpToken() != null && !userMcp.getMcpToken().isEmpty()) {
                    userMcp.setMcpToken(stringEncryptor.decrypt(userMcp.getMcpToken()));
                }
                userMcpList.add(userMcp);
            }
        }

        return userMcpList;
    }

    @Override
    @Transactional
    public List<McpUrl> getMcpUrlListByMcpIdList(List<McpId> mcpIdList) {
        List<McpUrl> mcpUrlList = new ArrayList<>();
        for(McpId mcpId : mcpIdList){
            mcpUrlList.add(mcpUrlMongoRepository.findByIdMcpId(mcpId.mcpId()).orElseThrow(() -> new RestApiException(UserMcpErrorStatus.INVALID_MCP_URL)));
        }
        return mcpUrlList;
    }

    @Override
    @Transactional
    public UserMcp getUserMcpToken(String userId, String platformId) {
        UserMcp userMcp = userMcpMongoRepository.findTopByIdUserIdAndPlatformId(userId, platformId).orElseThrow(() -> new RestApiException(UserMcpErrorStatus.MCP_NOT_YET_REGISTERED_FOR_USER));
        if (userMcp.getMcpToken() == null) {
            throw new RestApiException(UserMcpErrorStatus.UNREGISTERED_MCP_TOKEN);
        }

        userMcp.setMcpToken(stringEncryptor.decrypt(userMcp.getMcpToken()));
        return userMcp;
    }

    @Override
    @Transactional
    public UserMcp updateUserMcpToken(String userId, String platformId, UserMcpTokenUpdateRequest request) {
        List<UserMcp> userMcpList = userMcpMongoRepository.findByIdUserIdAndPlatformId(userId, platformId).orElseThrow(() -> new RestApiException(UserMcpErrorStatus.MCP_NOT_YET_REGISTERED_FOR_USER));
        String encryptedToken = stringEncryptor.encrypt(request.token());

        for (UserMcp userMcp : userMcpList) {
            Query query = new Query();
            query.addCriteria(
                    Criteria.where("_id.userId").is(userMcp.getId().getUserId())
                            .and("_id.mcpId").is(userMcp.getId().getMcpId())
                            .and("platformId").is(platformId)
            );

            Update update = new Update();
            update.set("mcpToken", encryptedToken);

            mongoTemplate.updateFirst(query, update, UserMcp.class);
        }

        return userMcpList.get(0);
    }

    @Override
    @Transactional
    public UserMcpTokenCheckResponse checkUserMcpToken(String userId, String mcpId) {
        UserMcp userMcp = userMcpMongoRepository.findByIdUserIdAndIdMcpId(userId, mcpId).orElseThrow(() -> new RestApiException(UserMcpErrorStatus.MCP_NOT_YET_REGISTERED_FOR_USER));
        String platformId = userMcp.getPlatformId();

        boolean result = false;
        List<UserMcp> platformMcpList = userMcpMongoRepository.findByIdUserIdAndPlatformId(userId, platformId).orElseThrow(() -> new RestApiException(UserMcpErrorStatus.MCP_NOT_YET_REGISTERED_FOR_USER));
        for (UserMcp platformMcp: platformMcpList) {
            if (platformMcp.getId().getUserId().equals(userId) && platformMcp.getId().getMcpId().equals(mcpId)) {
                continue;
            }

            if (platformMcp.getPlatformId().equals(platformId)) {
                userMcp.setMcpToken(platformMcp.getMcpToken());
                userMcpMongoRepository.save(userMcp);
                result = true;
                break;
            }
        }

        return UserMcpTokenCheckResponse.builder()
                .platformId(platformId)
                .isTokenExist(result)
                .build();
    }
}
