package com.mcphub.domain.workspace.controller;

import com.mcphub.domain.workspace.adviser.LlmTokenAdviser;
import com.mcphub.domain.workspace.dto.request.LlmTokenRequest;
import com.mcphub.domain.workspace.dto.response.api.LlmTokenListResponse;
import com.mcphub.domain.workspace.dto.response.api.LlmTokenSaveResponse;
import com.mcphub.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "LLM Token API", description = "사용자의 LLM Token을 저장하고 수정하는 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/llm")
public class LlmTokenController {
    private final LlmTokenAdviser llmTokenAdviser;

    @Operation(summary = "사용자 LLM Token을 불러오는 API", description = "사용자의 LLM Token을 모두 불러오는 API 입니다")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "LLM Token 불러오기 성공"
            )
    })
    @GetMapping(path = "/token")
    public BaseResponse<LlmTokenListResponse> getToken() {
        return BaseResponse.onSuccess(llmTokenAdviser.getToken());
    }

    @Operation(summary = "사용자 LLM Token 입력 API", description = "사용자의 LLM Token을 입력받아 저장하는 API 입니다")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "LLM Token 저장 성공"
            )
    })
    @Parameters({
            @Parameter(name = "llmId", description = "LLM의 ID 값"),
            @Parameter(name = "llmToken", description = "사용자의 LLM Token값"),
    })
    @PostMapping(path = "/token")
    public BaseResponse<LlmTokenSaveResponse> registerToken(
            @RequestBody LlmTokenRequest request
    ) {

        return BaseResponse.onSuccess(llmTokenAdviser.registerToken(request));
    }

    @Operation(summary = "사용자 LLM Token 수정 API", description = "사용자의 LLM Token을 입력받아 수정하는 API 입니다")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "LLM Token 수정 성공"
            )
    })
    @Parameters({
            @Parameter(name = "llmId", description = "LLM의 ID 값"),
            @Parameter(name = "llmToken", description = "사용자의 LLM Token값"),
    })
    @PatchMapping(path = "/token")
    public BaseResponse<LlmTokenSaveResponse> updateToken(
            @RequestBody LlmTokenRequest request
    ) {
        return BaseResponse.onSuccess(llmTokenAdviser.updateToken(request));
    }
}
