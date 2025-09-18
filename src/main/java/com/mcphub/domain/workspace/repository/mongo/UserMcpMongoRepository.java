package com.mcphub.domain.workspace.repository.mongo;

import com.mcphub.domain.workspace.entity.UserMcp;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserMcpMongoRepository extends MongoRepository<UserMcp, String> {
    UserMcp findByUserIdAndMcpId(String userId, String mcpId);
}
