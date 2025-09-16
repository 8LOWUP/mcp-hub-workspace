package com.mcphub.domain.workspace.entity;

import com.mcphub.domain.workspace.common.McpInfo;
import com.mcphub.global.common.base.BaseDocument;
import jakarta.persistence.EntityListeners;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "workspace")
@Getter
@Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Workspace extends BaseDocument implements Persistable<String> {

    @Id
    private String id;
    private String llmId;   // bigint -> String
    private String userId;  // bigint -> String
    private String title;   // text

    private List<McpInfo> mcps = new ArrayList<>();

    @Override
    public boolean isNew() {
        return getCreatedAt() == null;
    }
}