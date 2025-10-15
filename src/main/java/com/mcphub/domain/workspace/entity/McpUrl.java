package com.mcphub.domain.workspace.entity;

import com.mcphub.global.common.base.BaseDocument;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "mcpUrl")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class McpUrl extends BaseDocument implements Persistable<McpUrl.McpUrlId> {

    @Id
    private McpUrlId id;
    private String mcpUrl;

    @Override
    public boolean isNew() {
        return getCreatedAt() == null;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class McpUrlId {
        private String mcpId;
    }
}