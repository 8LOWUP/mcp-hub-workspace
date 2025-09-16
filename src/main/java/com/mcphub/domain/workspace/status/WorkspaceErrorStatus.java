package com.mcphub.domain.workspace.status;

import com.mcphub.global.common.exception.code.BaseCodeDto;
import com.mcphub.global.common.exception.code.BaseCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum WorkspaceErrorStatus implements BaseCodeInterface {

    USER_ID_NOT_FOUND_IN_TOKEN(HttpStatus.BAD_REQUEST, "WORKSPACE400", "토큰에 유저 정보가 없습니다."),
    MCP_NUMBER_TOLERANCE_EXCEEDED(HttpStatus.BAD_REQUEST, "WORKSPACE400", "워크스페이스 당 허용된 MCP 개수를 초과하였습니다."),
    MISMATCH_WORKSPACE_AND_USER(HttpStatus.BAD_REQUEST, "WORKSPACE400", "워크스페이스 사용자 정보가 일치하지 않습니다."),
    WORKSPACE_PARAMETER_ERROR(HttpStatus.BAD_REQUEST, "WORKSPACE400", "워크스페이스 생성에 필요한 매개변수값이 잘못되었습니다."),
    DELETED_WORKSPACE(HttpStatus.BAD_REQUEST, "WORKSPACE400", "잘못된 워크스페이스를 조회하였습니다."),
    WORKSPACE_NOT_FOUND(HttpStatus.NOT_FOUND, "WORKSPACE404", "워크스페이스를 찾을 수 없습니다."),

    DB_SAVE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "REGISTER001", "DB 저장중 문제 발생. RollBack 됩니다."),

    ;

    private final HttpStatus httpStatus;
    private final boolean isSuccess = false;
    private final String code;
    private final String message;

    @Override
    public BaseCodeDto getCode() {
        return BaseCodeDto.builder()
                .httpStatus(httpStatus)
                .isSuccess(isSuccess)
                .code(code)
                .message(message)
                .build();
    }


}
