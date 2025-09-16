package com.mcphub.domain.file.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.mcphub.domain.file.dto.FileCreateResponse;
import com.mcphub.domain.file.service.FileService;
import com.mcphub.global.common.base.BaseResponse;

@Tag(name = "File API", description = "파일 관련 API (파일 업로드 작업은, 파일 업로드, PreSigned URL 둘 중 편하신 걸로 해주시면 됩니다.)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;

    @PostMapping(value = "/{category}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "파일 업로드 API", description = "파일을 생성하는 API입니다. jpg, mp3, mp4 등 다양한 파일을 업로드할 수 있습니다.")
    public BaseResponse<FileCreateResponse> createFile
            (@PathVariable("category") String category,
             @Valid @Parameter(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
             @RequestPart("file") MultipartFile file
            ) {
        return BaseResponse.onSuccess(fileService.createFile(category, file));
    }

    @PostMapping("/presigned-url/{category}")
    @Operation(summary = "PreSigned Url 생성 API", description = "AWS S3에 파일 업로드를 위한 PreSigned URL을 생성합니다. jpg, mp3, mp4 등 다양한 파일을 업로드할 수 있습니다.")
    public BaseResponse<FileCreateResponse> createPresignedUrl(
            @PathVariable("category") String category,
            @RequestParam("fileName") String fileName
    ) {
        return BaseResponse.onSuccess(fileService.createPresignedUrl(category, fileName));
    }

    @DeleteMapping
    @Operation(summary = "파일 삭제 API", description = "파일을 삭제하는 API입니다. 파일 URL을 입력하면 해당 파일을 삭제합니다.")
    public BaseResponse<String> deleteFile(@RequestParam("fileUrl") String fileUrl) {
        fileService.deleteFile(fileUrl);
        return BaseResponse.onSuccess("파일 삭제 성공");
    }
}
