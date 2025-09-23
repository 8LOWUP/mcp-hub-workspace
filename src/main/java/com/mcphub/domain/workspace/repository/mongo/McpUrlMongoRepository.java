package com.mcphub.domain.workspace.repository.mongo;

import com.mcphub.domain.workspace.dto.McpId;
import com.mcphub.domain.workspace.entity.McpUrl;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface McpUrlMongoRepository  extends MongoRepository<McpUrl, String> {
    Optional<McpUrl> findByMcpId(String mcpId);
    boolean existsByMcpId(String mcpId);
}
