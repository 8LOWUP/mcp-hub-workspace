package com.mcphub.domain.workspace.entity;

import com.mcphub.global.common.base.BaseDocument;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chat")
@Getter
@Setter
@Builder
public class Chat extends BaseDocument implements Persistable<String> {

    @Id
    private String id;
    private String workspaceId;
    private String chat;
    private boolean isRequest;

    @Override
    public boolean isNew() {
        return getCreatedAt() == null;
    }
}
