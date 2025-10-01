package com.mcphub.domain.workspace.repository.mongo;

import com.mcphub.domain.workspace.entity.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMongoRepository extends MongoRepository<Chat, String> {
    List<Chat> findByWorkspaceId(String workspaceId);
    Page<Chat> findByWorkspaceId(String workspaceId,
                                 Pageable pageable);
    Page<Chat> findByWorkspaceIdOrderByCreatedAtDesc(String workspaceId, Pageable pageable);
}
