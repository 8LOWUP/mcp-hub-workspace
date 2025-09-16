package com.mcphub.domain.file.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.mcphub.global.common.base.BaseEntity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String fileName;

    private String fileOriginName;

    private String filePath;

    private String fileType;
}

