package com.mcphub.domain.workspace.repository.mongo;

import com.mcphub.domain.workspace.entity.UserMcp;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserMcpMongoRepository extends MongoRepository<UserMcp,UserMcp.IdClass> {
    Optional<UserMcp> findByUserIdAndMcpId(String userId, String mcpId);
    Optional<List<UserMcp>> findByUserIdAndPlatformId(String userId, String platformId);
    Optional<UserMcp> findTopByUserIdAndPlatformId(String userId, String platformId);

    List<UserMcp> deleteByMcpId(String mcpId);
    boolean existsByMcpIdAndUserId(String mcpId, String userId);
    UserMcp deleteByMcpIdAndUserId(String userId, String mcpId);
}
