package com.mcphub.domain.workspace.controller;

import com.mcphub.domain.workspace.adviser.WorkspaceAdviser;
import com.mcphub.domain.workspace.dto.request.*;
import com.mcphub.domain.workspace.dto.response.*;
import com.mcphub.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "워크스페이스 API", description = "워크스페이스 인스턴스 CRUD API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/workspaces")
public class WorkspaceController {
    private final WorkspaceAdviser workspaceAdviser;

    @Operation(summary = "워크스페이스 생성 API", description = "새로운 워크스페이스 생성에 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "워크스페이스 생성 성공"),
            @ApiResponse(responseCode = "400", description = "토큰을 등록하지 않은 llm에 접근", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "워크스페이스 생성에 실패했습니다.", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "502", description = "채팅 요청에 실패했습니다.", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping()
    public BaseResponse<WorkspaceCreateResponse> createWorkspace(
            @Parameter(description = "워크스페이스 생성 정보", required = true) @RequestBody WorkspaceCreateRequest request
    ) {
        return BaseResponse.onSuccess(workspaceAdviser.createWorkspace(request));
    }

    @Operation(summary = "워크스페이스 히스토리 조회 API", description = "워크스페이스 히스토리를 조회할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "워크스페이스 히스토리 조회 성공"),
            @ApiResponse(responseCode = "400", description = "토큰에서 유저 정보를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping()
    public BaseResponse<List<WorkspaceHistoryResponse>> getWorkspaceHistory() {
        return BaseResponse.onSuccess(workspaceAdviser.getWorkspaceHistory());
    }

    @Operation(summary = "워크스페이스 상세 조회 API", description = "특정 워크스페이스에 대한 상세 정보를 조회할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "워크스페이스 상세 조회 성공"),
            @ApiResponse(responseCode = "404", description = "워크스페이스를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "해당 워크스페이스에 대한 접근 권한이 없습니다.", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "삭제된 워크스페이스입니다.", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping(path = "/{workspaceId}")
    public BaseResponse<WorkspaceDetailResponse> getWorkspaceDetail(
            @Parameter(description = "워크스페이스 ID", required = true) @PathVariable("workspaceId") String workspaceId
    ) {
        return BaseResponse.onSuccess(workspaceAdviser.getWorkspaceDetail(workspaceId));
    }

    @Operation(summary = "워크스페이스 제목 수정 API", description = "워크스페이스 제목을 수정할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "워크스페이스 제목 수정 성공"),
            @ApiResponse(responseCode = "404", description = "워크스페이스를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "삭제된 워크스페이스입니다.", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "해당 워크스페이스에 대한 접근 권한이 없습니다.", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PatchMapping(path = "/{workspaceId}")
    public BaseResponse<WorkspaceUpdateResponse> updateWorkspaceName(
            @Parameter(description = "워크스페이스 ID", required = true) @PathVariable("workspaceId") String workspaceId,
            @Parameter(description = "수정할 워크스페이스 제목", required = true) @RequestBody WorkspaceUpdateRequest request
    ) {
        return BaseResponse.onSuccess(workspaceAdviser.updateWorkspaceName(workspaceId, request));
    }

    @Operation(summary = "워크스페이스 내 MCP 활성화 여부 수정 API", description = "워크스페이스 내 MCP 활성화 여부를 수정할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MCP 활성화 여부 수정 성공"),
            @ApiResponse(responseCode = "404", description = "워크스페이스를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "삭제된 워크스페이스입니다.", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "해당 워크스페이스에 대한 접근 권한이 없습니다.", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PatchMapping(path = "/{workspaceId}/mcps")
    public BaseResponse<String> updateActivatedMcpsInWorkspace(
            @Parameter(description = "워크스페이스 ID", required = true) @PathVariable("workspaceId") String workspaceId,
            @Parameter(description = "MCP 활성화 정보", required = true) @RequestBody WorkspaceMcpUpdateRequest request
    ) {
        if (workspaceAdviser.updateActivatedMcpsInWorkspace(workspaceId, request))
            return BaseResponse.onSuccess("워크스페이스 mcp 활성화 수정 성공");
        return BaseResponse.onFailure(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()), "요청에 실패하였습니다.", null);
    }

    @Operation(summary = "워크스페이스 내 LLM 변경 API", description = "워크스페이스 내 LLMd을 변경할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "LLM 변경 성공"),
            @ApiResponse(responseCode = "404", description = "워크스페이스를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "삭제된 워크스페이스입니다.", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "해당 워크스페이스에 대한 접근 권한이 없습니다.", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "토큰을 등록하지 않은 llm에 접근하고 있습니다.", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PatchMapping(path = "/{workspaceId}/llm")
    public BaseResponse<String> updateActivatedLlmInWorkspace(
            @Parameter(description = "워크스페이스 ID", required = true) @PathVariable("workspaceId") String workspaceId,
            @Parameter(description = "변경할 Llm 정보", required = true) @RequestBody WorkspaceLlmUpdateRequest request
    ) {
        if (workspaceAdviser.updateActivatedLlmInWorkspace(workspaceId, request))
            return BaseResponse.onSuccess("워크스페이스 LLM 변경 성공");
        return BaseResponse.onFailure(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()), "요청에 실패하였습니다.", null);
    }

    @Operation(summary = "워크스페이스 삭제 API", description = "워크스페이스를 삭제할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "워크스페이스 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "워크스페이스를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "이미 삭제된 워크스페이스입니다.", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "해당 워크스페이스에 대한 접근 권한이 없습니다.", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @DeleteMapping(path = "/{workspaceId}")
    public BaseResponse<String> deleteWorkspace(
            @Parameter(description = "워크스페이스 ID", required = true) @PathVariable("workspaceId") String workspaceId
    ) {
        if (workspaceAdviser.deleteWorkspace(workspaceId)) return BaseResponse.onSuccess("워크스페이스 삭제 성공");
        return BaseResponse.onFailure(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()), "요청에 실패하였습니다.", null);
    }

    @Operation(summary = "채팅 요청 API", description = "채팅 요청을 보내고 결과를 받는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "채팅 요청 성공"),
            @ApiResponse(responseCode = "404", description = "워크스페이스를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "502", description = "채팅 요청에 실패했습니다.", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping(path = "/{workspaceId}/chats")
    public BaseResponse<WorkspaceChatResponse> sendChat(
            @Parameter(description = "워크스페이스 ID", required = true) @PathVariable("workspaceId") String workspaceId,
            @Parameter(description = "채팅 메시지", required = true) @RequestBody WorkspaceChatRequest request
    ) {
        return BaseResponse.onSuccess(workspaceAdviser.sendChat(workspaceId, request));
    }

    @Operation(summary = "채팅 기록 요청 API", description = "이전 채팅 기록을 받는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "채팅 기록 요청 성공"),
            @ApiResponse(responseCode = "404", description = "워크스페이스를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping(path = "/{workspaceId}/chats")
    public BaseResponse<Page<WorkspaceChatHistoryResponse>> getChatHistory(
            @Parameter(description = "워크스페이스 ID", required = true) @PathVariable("workspaceId") String workspaceId,
            @Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "50") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return BaseResponse.onSuccess(workspaceAdviser.getChatHistory(workspaceId, pageable));
    }
}
