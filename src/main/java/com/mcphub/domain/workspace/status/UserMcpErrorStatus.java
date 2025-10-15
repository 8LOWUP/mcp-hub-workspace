package com.mcphub.domain.workspace.status;

import com.mcphub.global.common.exception.code.BaseCodeDto;
import com.mcphub.global.common.exception.code.BaseCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserMcpErrorStatus implements BaseCodeInterface {
    MCP_NOT_YET_REGISTERED_FOR_USER(HttpStatus.BAD_REQUEST, "UMP001", "유저가 등록하지 않은 MCP입니다."),
    INVALID_MCP_URL(HttpStatus.BAD_REQUEST, "UMP002", "MCP가 존재하지 않습니다."),
    UNREGISTERED_MCP_TOKEN(HttpStatus.BAD_REQUEST, "UMP003", "아직 MCP 토큰이 등록되지 않았습니다."),
    MCP_PLATFORM_ID_NOT_FOUND(HttpStatus.NO_CONTENT, "UMP004", "MCP 플랫폼 아이디가 존재하지 않습니다.")
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
