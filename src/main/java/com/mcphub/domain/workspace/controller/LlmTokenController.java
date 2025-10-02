package com.mcphub.domain.workspace.controller;

import com.mcphub.domain.workspace.adviser.LlmTokenAdviser;
import com.mcphub.domain.workspace.dto.request.LlmTokenRequest;
import com.mcphub.domain.workspace.dto.response.api.LlmTokenListResponse;
import com.mcphub.domain.workspace.dto.response.api.LlmTokenResponse;
import com.mcphub.domain.workspace.dto.response.api.LlmTokenSaveResponse;
import com.mcphub.domain.workspace.entity.enums.Llm;
import com.mcphub.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "LLM Token API", description = "사용자의 LLM Token을 저장하고 수정하는 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/workspaces/llm")
public class LlmTokenController {
    private final LlmTokenAdviser llmTokenAdviser;

    @Operation(summary = "사용자 LLM Token을 불러오는 API", description = "사용자의 LLM Token을 불러오는 API 입니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "LLM Token 불러오기 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "등록되지 않은 토큰에 접근", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping(path = "/token/{llmId}")
    public BaseResponse<LlmTokenResponse> getToken(
            @PathVariable("llmId") Llm llmId
    ) {
        return BaseResponse.onSuccess(llmTokenAdviser.getToken(llmId));
    }

    @Operation(summary = "사용자 LLM Token 신규 입력 API", description = "사용자의 LLM Token을 입력받아 저장하는 API 입니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "LLM Token 저장 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 LLM 토큰", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "409", description = "이미 토큰이 존재",content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Parameters({
            @Parameter(name = "llmId", description = "LLM의 ID 값"),
    })
    @PostMapping(path = "/token/{llmId}")
    public BaseResponse<LlmTokenSaveResponse> registerToken(
            @PathVariable("llmId") Llm llmId,
            @RequestBody LlmTokenRequest request
    ) {

        return BaseResponse.onSuccess(llmTokenAdviser.registerToken(llmId, request));
    }

    @Operation(summary = "사용자 LLM Token 수정 API", description = "사용자의 LLM Token을 입력받아 수정하는 API 입니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "LLM Token 수정 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 LLM 토큰", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
    })
    @Parameters({
            @Parameter(name = "llmId", description = "LLM의 ID 값"),
    })
    @PatchMapping(path = "/token/{llmId}")
    public BaseResponse<LlmTokenSaveResponse> updateToken(
            @PathVariable("llmId") Llm llmId,
            @RequestBody LlmTokenRequest request
    ) {
        return BaseResponse.onSuccess(llmTokenAdviser.updateToken(llmId, request));
    }
}
