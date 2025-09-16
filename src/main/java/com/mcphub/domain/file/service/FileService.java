package com.mcphub.domain.file.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.mcphub.domain.file.dto.FileCreateResponse;
import com.mcphub.global.util.S3FileUtil;

@Service
@RequiredArgsConstructor
public class FileService {

    private final S3FileUtil s3FileUtil;

    // todo : 파일 메타데이터 파일 엔티티에 저장 필요

    public FileCreateResponse createFile(String domain, final MultipartFile file) {
        String imageUrl = s3FileUtil.uploadFile(domain, file);
        return new FileCreateResponse(imageUrl);
    }

    public FileCreateResponse createPresignedUrl(String domain, String fileName) {
        String presignedUrl = s3FileUtil.getPresignedUrl(domain, fileName);
        return new FileCreateResponse(presignedUrl);
    }

    public void deleteFile(String fileUrl) {
        s3FileUtil.deleteFile(fileUrl);
    }

}
