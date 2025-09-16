package com.mcphub.domain.member.status;

import com.mcphub.global.common.exception.code.BaseCodeDto;
import com.mcphub.global.common.exception.code.BaseCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorStatus implements BaseCodeInterface {
    DUPLICATED_CLIENT_ID(HttpStatus.BAD_REQUEST, "MEMBER400", "중복되는 아이디입니다."),
    UNSUPPORTED_LOGIN_TYPE(HttpStatus.BAD_REQUEST, "MEMBER400", "지원하지 않는 로그인 타입입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER404", "회원을 찾을 수 없습니다."),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "MEMBER401", "로그인 정보를 찾을 수 없습니다."),

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
