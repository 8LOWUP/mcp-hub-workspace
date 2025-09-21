package com.mcphub.domain.workspace.repository.mongo;

import com.mcphub.domain.workspace.entity.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMongoRepository extends MongoRepository<Chat, String> {
    List<Chat> findTopNByWorkspaceIdOrderByCreatedAtDesc(String workspaceId, int n);
}
