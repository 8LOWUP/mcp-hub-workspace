package com.mcphub.domain.workspace.entity;

import com.mcphub.global.common.base.BaseDocument;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "userMcp")
@Getter
@Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
public class UserMcp extends BaseDocument implements Persistable<String> {
    @Id
    private String id;
    private String userId;
    private String mcpId;
    private String mcpToken;
    private String requestUrl;

    @Override
    public boolean isNew() {
        return getCreatedAt() == null;
    }
}
