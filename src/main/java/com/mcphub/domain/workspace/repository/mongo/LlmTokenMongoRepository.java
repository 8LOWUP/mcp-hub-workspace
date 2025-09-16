package com.mcphub.domain.workspace.repository.mongo;

import com.mcphub.domain.workspace.entity.LlmToken;
import com.mcphub.domain.workspace.entity.enums.Llm;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LlmTokenMongoRepository extends MongoRepository<LlmToken, String> {
    boolean existsByUserIdAndLlmId(String userId, Llm llmId);
    List<LlmToken> findByUserId(String userId);
    LlmToken findByUserIdAndLlmId(String userId, Llm llmId);
}
