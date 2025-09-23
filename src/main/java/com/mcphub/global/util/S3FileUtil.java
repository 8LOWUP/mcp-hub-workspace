package com.mcphub.global.util;


import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import org.springframework.stereotype.Component;
import com.mcphub.global.common.exception.RestApiException;
import com.mcphub.global.common.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import static com.mcphub.global.common.exception.code.status.GlobalErrorStatus._FAILED_NOT_VALID_FORMAT;

@RequiredArgsConstructor
@Component
@Slf4j
public class S3FileUtil {
    private final AmazonS3Client amazonS3Client; // AmazonS3Client 객체 주입
    @Value("${cloud.aws.s3.bucket}") // S3 버킷 이름
    private String bucket;

    // 파일 업로드
    public String uploadFile(String category, MultipartFile multipartFile) {
        // 파일명
        String fileName = createFileName(category, Objects.requireNonNull(multipartFile.getOriginalFilename()));

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());

        // 파일 타입 확인
        boolean isImage = multipartFile.getContentType() != null && multipartFile.getContentType().startsWith("image");
        if(!isImage) throw new RestApiException(_FAILED_NOT_VALID_FORMAT);
        // 바이트 배열로 파일 내용 읽기
        byte[] bytes;
        try {
            bytes = multipartFile.getBytes();
        } catch (IOException e) {
            // 파일 읽기 실패
            log.error(e.getMessage());
            throw new RestApiException(GlobalErrorStatus._FAILED_READ_FILE);
        }

        // Content-Length 설정
        objectMetadata.setContentLength(bytes.length);

        // ByteArrayInputStream 생성
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        // S3에 업로드
        try {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, byteArrayInputStream, objectMetadata));
        } catch (AmazonS3Exception e) {
            // S3 업로드 실패 시 에러 코드 분기
            log.error(e.getMessage());
            throw new RestApiException(GlobalErrorStatus._S3_UPLOAD_ERROR);
        }

        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // Presigned URL 생성 todo: 위에 직접 파일 올리는 거랑 공통 로직 분리하기
    public String getPresignedUrl(String category, String originFileName) {
        // 파일명
        String fileName = createFileName(category, originFileName);

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, fileName);

        generatePresignedUrlRequest.setMethod(HttpMethod.GET);
        generatePresignedUrlRequest.setExpiration(getExpiration());  // 1시간 유효 기간 설정
        return amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }

    //이미지 삭제
    public void deleteFile(String fileUrl) {
        String[] deleteUrl = fileUrl.split("/", 4);
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, deleteUrl[3]));
    }

    //파일명 생성
    private String createFileName(String category, String originalFileName) {
        int fileExtensionIndex = originalFileName.lastIndexOf(".");
        String fileExtension = originalFileName.substring(fileExtensionIndex);
        String fileName = originalFileName.substring(0, fileExtensionIndex);
        String random = String.valueOf(UUID.randomUUID());

        // 파일명 생성, 같은 파일명이 있을 경우 덮어쓰기 방지
        if(category.isEmpty()){
            return fileName + "_" + random + fileExtension;
        }
        return category + "/" + fileName + "_" + random + fileExtension;
    }

    // 유효 기간 설정
    // Presigned URL 유효 기간 설정 (1시간)
    private Date getExpiration() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1);  // 현재 시간에 1시간 추가
        return calendar.getTime();
    }

}
