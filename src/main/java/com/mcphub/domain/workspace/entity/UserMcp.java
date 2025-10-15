package com.mcphub.domain.workspace.entity;

import com.mcphub.global.common.base.BaseDocument;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "userMcp")
@Getter
@Setter
@Builder
public class UserMcp extends BaseDocument implements Persistable<UserMcp.IdClass> {
    @Id
    private IdClass id;

    private String platformId;
    private String mcpToken;

    @Override
    public boolean isNew() {
        return getCreatedAt() == null;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IdClass {
        private String userId;
        private String mcpId;
    }
}
