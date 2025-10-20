package com.mcphub.domain.workspace.entity;

import com.mcphub.global.common.base.BaseDocument;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "userMcp")
@CompoundIndex(name = "user_mcp_idx", def = "{'id.userId': 1, 'id.mcpId': 1}", unique = true)
@Getter
@Setter
@Builder
public class UserMcp extends BaseDocument implements Persistable<UserMcp.IdClass> {
    @Id
    private IdClass id;

    private String platformId;
    private String mcpToken;

    @Field("saved")
    private Boolean saved = false;

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
