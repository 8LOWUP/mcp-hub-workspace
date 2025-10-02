package com.mcphub.domain.workspace.controller;

import com.mcphub.domain.workspace.adviser.UserMcpAdviser;
import com.mcphub.domain.workspace.dto.request.UserMcpTokenUpdateRequest;
import com.mcphub.domain.workspace.dto.response.UserMcpTokenCheckResponse;
import com.mcphub.domain.workspace.dto.response.UserMcpTokenGetResponse;
import com.mcphub.domain.workspace.dto.response.UserMcpTokenUpdateResponse;
import com.mcphub.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Mcp API", description = "사용자의 MCP Token을 저장하고 수정하는 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/workspaces/mcps")
public class UserMcpController {
    private final UserMcpAdviser userMcpAdviser;

    @Operation(summary = "MCP 토큰 조회 API", description = "사용자의 특정 MCP 토큰을 조회하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "MCP 토큰 조회 성공"),
            @ApiResponse(responseCode = "400", description =  "MCP 토큰이 존재하지 않음")
    })
    @GetMapping("/token/{platformId}")
    public BaseResponse<UserMcpTokenGetResponse> getUserMcpToken(
            @PathVariable("platformId") String mcpId
    ) {
        return BaseResponse.onSuccess(userMcpAdviser.getUserMcpToken(mcpId));
    }

    @Operation(summary = "MCP 토큰 업데이트 API", description = "사용자의 MCP 토큰을 등록하거나 변경하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "MCP 토큰 저장 성공"),
            @ApiResponse(responseCode = "400", description =  "유저가 저장하지 않은 MCP에 토큰 저장 시도")
    })
    @PostMapping("/token/{platformId}")
    public BaseResponse<UserMcpTokenUpdateResponse> updateUserMcpToken(
            @PathVariable("platformId") String mcpId,
            @RequestBody UserMcpTokenUpdateRequest request
    ) {
        return BaseResponse.onSuccess(userMcpAdviser.updateUserMcpToken(mcpId, request));
    }

    @Operation(summary = "MCP 토큰 존재 여부 확인 API", description = "사용자의 MCP 토큰이 이미 등록된지 확인하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공"),
            @ApiResponse(responseCode = "400", description =  "유저가 저장하지 않은 MCP에 토큰 접근 시도")
    })
    @GetMapping("/token/check/{mcpId}")
    public BaseResponse<UserMcpTokenCheckResponse> checkUserMcpToken(
            @PathVariable("mcpId") String mcpId
    ) {
        return BaseResponse.onSuccess(userMcpAdviser.checkUserMcpToken(mcpId));
    }
}
