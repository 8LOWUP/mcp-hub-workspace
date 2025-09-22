package com.mcphub.domain.workspace.entity;

import com.mcphub.domain.workspace.entity.enums.Llm;
import com.mcphub.global.common.base.BaseDocument;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "llmToken")
@Getter
@Setter
@Builder
public class LlmToken extends BaseDocument implements Persistable<String> {
    @Id
    private String id;
    private String userId;
    private Llm llmId;
    private String token;

    @Override
    public boolean isNew() {
        return getCreatedAt() == null;
    }
}
