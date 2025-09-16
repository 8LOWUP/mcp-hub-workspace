package com.mcphub.domain.workspace.status;

import com.mcphub.global.common.exception.code.BaseCodeDto;
import com.mcphub.global.common.exception.code.BaseCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum LlmErrorStatus implements BaseCodeInterface {

    INVALID_LLM_TOKEN(HttpStatus.BAD_REQUEST, "LLM001", "LLM Token 값이 유효하지 않습니다."),
    TOKEN_ALREADY_EXISTS(HttpStatus.CONFLICT, "LLM002", "이미 토큰이 등록된 기록이 있습니다."),
    TOKEN_NOT_EXISTS(HttpStatus.BAD_REQUEST, "LLM003", "등록된 토큰이 존재하지 않습니다.")
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
