package com.mcphub.domain.workspace.repository.mongo;

import com.mcphub.domain.workspace.entity.UserMcp;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserMcpMongoRepository extends MongoRepository<UserMcp,UserMcp.IdClass> {
    Optional<UserMcp> findByIdUserIdAndIdMcpId(String userId, String mcpId);
    Optional<List<UserMcp>> findByIdUserIdAndPlatformId(String userId, String platformId);
    Optional<UserMcp> findTopByIdUserIdAndPlatformId(String userId, String platformId);
}
