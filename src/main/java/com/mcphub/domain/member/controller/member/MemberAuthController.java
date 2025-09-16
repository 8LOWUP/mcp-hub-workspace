package com.mcphub.domain.member.controller.member;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.mcphub.domain.member.adviser.member.MemberAuthAdviser;
import com.mcphub.domain.member.dto.response.api.SocialLoginResponse;
import com.mcphub.global.common.base.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member Auth API", description = "Member Auth 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class MemberAuthController {

    private final MemberAuthAdviser memberAuthAdviser;

    @Operation(summary = "카카오 소셜 로그인 API", description = "카카오 로그인을 수행하는 API입니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "카카오 소셜 로그인 성공"
            )
    })
    @Parameters({
            @Parameter(name = "code", description = "인증 코드를 받아옵니다."),
    })
    @GetMapping("/social/kakao")
    public BaseResponse<SocialLoginResponse> kakaoLogin(
            @RequestParam String code) {
        return BaseResponse.onSuccess(memberAuthAdviser.kakaoLogin(code));
    }

    @Operation(summary = "구글 소셜 로그인 API", description = "구글 로그인을 수행하는 API입니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "구글 소셜 로그인 성공"
            )
    })
    @Parameters({
            @Parameter(name = "code", description = "인증 코드를 받아옵니다."),
    })
    @GetMapping("/social/google")
    public BaseResponse<SocialLoginResponse> googleLogin(
            @RequestParam String code) {
        return BaseResponse.onSuccess(memberAuthAdviser.googleLogin(code));
    }

    @Operation(summary = "accessToken, refreshToken 재발급 API", description = "refreshToken가 유효하다면 새로운 accessToken을 발급하는 API입니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "accessToken 재발급 성공"
            )
    })
    @Parameters({
            @Parameter(name = "refreshToken", description = "로그인시 받는 refreshToken"),
    })
    @PostMapping("/token/reissue")
    public BaseResponse<SocialLoginResponse> reissue(@RequestParam String refreshToken) {
        return BaseResponse.onSuccess(memberAuthAdviser.regenerateToken(refreshToken));
    }


    //
    // @Operation(summary = "로그아웃 API", description = "해당 유저의 refreshToken을 삭제하는 API입니다.")
    // @DeleteMapping("/logout")
    // public BaseResponse<MemberIdResponse> logout(
    //         @CurrentMember Member member
    // ) {
    //     return BaseResponse.onSuccess(memberAuthAdviser.logout(member));
    // }
    //
    // @Operation(summary = "회원 탈퇴 API", description = "해당 유저 정보를 삭제하는 API입니다.")
    // @DeleteMapping
    // public BaseResponse<MemberIdResponse> withdrawal(
    //         @CurrentMember Member member
    // ) {
    //     return BaseResponse.onSuccess(memberAuthAdviser.withdrawal(member));
    // }

}

