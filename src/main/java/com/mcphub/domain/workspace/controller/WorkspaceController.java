package com.mcphub.domain.workspace.controller;

import com.mcphub.domain.workspace.adviser.WorkspaceAdviser;
import com.mcphub.domain.workspace.dto.request.WorkspaceCreateRequest;
import com.mcphub.domain.workspace.dto.request.WorkspaceMcpUpdateRequest;
import com.mcphub.domain.workspace.dto.request.WorkspaceUpdateRequest;
import com.mcphub.domain.workspace.dto.response.WorkspaceCreateResponse;
import com.mcphub.domain.workspace.dto.response.WorkspaceDetailResponse;
import com.mcphub.domain.workspace.dto.response.WorkspaceHistoryResponse;
import com.mcphub.domain.workspace.dto.response.WorkspaceUpdateResponse;
import com.mcphub.domain.workspace.status.WorkspaceErrorStatus;
import com.mcphub.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "워크스페이스 생성 성공")
    })
    @PostMapping()
    public BaseResponse<WorkspaceCreateResponse> createWorkspace(
            @RequestBody WorkspaceCreateRequest request
    ) {
        return BaseResponse.onSuccess(workspaceAdviser.createWorkspace(request));
    }

    @Operation(summary = "워크스페이스 히스토리 조회 API", description = "워크스페이스 히스토리를 조회할 때 사용하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "워크스페이스 히스토리 조회 성공")
    })
    @GetMapping()
    public BaseResponse<List<WorkspaceHistoryResponse>> getWorkspaceHistory() {

        return BaseResponse.onSuccess(workspaceAdviser.getWorkspaceHistory());
    }

    @Operation(summary = "워크스페이스 상세 조회 API", description = "특정 워크스페이스에 대한 상세 정보를 조회할 때 사용하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "워크스페이스 상세 조회 성공")
    })
    @GetMapping(path = "/{workspaceId}")
    public BaseResponse<WorkspaceDetailResponse> getWorkspaceDetail(
            @PathVariable("workspaceId") String workspaceId
    ) {
        return BaseResponse.onSuccess(workspaceAdviser.getWorkspaceDetail(workspaceId));
    }

    @Operation(summary = "워크스페이스 제목 수정 API", description = "워크스페이스 제목을 수정할 때 사용하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "워크스페이스 제목 수정 성공")
    })
    @PatchMapping(path = "/{workspaceId}")
    public BaseResponse<WorkspaceUpdateResponse> updateWorkspaceName(
            @PathVariable("workspaceId") String workspaceId,
            @RequestBody WorkspaceUpdateRequest request
    ) {
        return BaseResponse.onSuccess(workspaceAdviser.updateWorkspaceName(workspaceId, request));
    }

    @Operation(summary = "워크스페이스 내 MCP 활성화 여부 수정 API", description = "워크스페이스 내 MCP 활성화 여부를 수정할 때 사용하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "MCP 활성화 여부 수정 성공")
    })
    @PatchMapping(path = "/{workspaceId}/mcps")
    public BaseResponse<String> updateActivatedMcpsInWorkspace(
            @PathVariable("workspaceId") String workspaceId,
            @RequestBody WorkspaceMcpUpdateRequest request
    ) {
        if (workspaceAdviser.updateActivatedMcpsInWorkspace(workspaceId, request))
            return BaseResponse.onSuccess("워크스페이스 mcp 활성화 수정 성공");
        return BaseResponse.onFailure(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()), "요청에 실패하였습니다.", null);
    }

    @Operation(summary = "워크스페이스 삭제 API", description = "워크스페이스를 삭제할 때 사용하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "워크스페이스 삭제 성공")
    })
    @DeleteMapping(path = "/{workspaceId}")
    public BaseResponse<String> deleteWorkspace(
            @PathVariable("workspaceId") String workspaceId
    ) {
        if (workspaceAdviser.deleteWorkspace(workspaceId)) return BaseResponse.onSuccess("워크스페이스 삭제 성공");
        return BaseResponse.onFailure(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()), "요청에 실패하였습니다.", null);
    }

}
