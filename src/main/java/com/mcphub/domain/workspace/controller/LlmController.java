package com.mcphub.domain.workspace.controller;

import com.mcphub.domain.workspace.adviser.LlmAdviser;
import com.mcphub.domain.workspace.dto.response.api.LlmResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "LLM Token API", description = "LLM 목록에 접근하는 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/workspaces/llm")
public class LlmController {
    private final LlmAdviser llmAdviser;

    @Operation(summary = "사용가능한 모든 LLM 모델을 조회하는 API", description = "사용가능한 모든 LLM 모델을 조회하는 API 입니다")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "LLM 모델 목록 불러오기 성공"
            )
    })
    @GetMapping
    public LlmResponse getLlmList() {
        return llmAdviser.getLlmList();
    }
}
