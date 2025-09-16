// package com.mcphub.domain.member.controller.member;
//
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.Parameter;
// import io.swagger.v3.oas.annotations.responses.ApiResponse;
// import io.swagger.v3.oas.annotations.responses.ApiResponses;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import lombok.RequiredArgsConstructor;
// import org.springframework.http.MediaType;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;
// import com.mcphub.domain.member.adviser.member.MemberAdviser;
// import com.mcphub.domain.member.dto.response.member.common.MemberIdResponse;
// import com.mcphub.domain.member.entity.Member;
// import com.mcphub.global.common.base.BaseResponse;
// import com.mcphub.global.config.security.auth.CurrentMember;
//
// @Tag(name = "일반 사용자(App)용 Member API", description = "일반 사용자(App)용 Member 관련 API")
// @RestController
// @RequiredArgsConstructor
// @RequestMapping("/members")
// public class MemberMemberController {
//     private final MemberAdviser memberAdviser;
//
//     @Operation(summary = "사용자 프로필 사진 변경 API", description = "사용자의 사진 변경하는 API 입니다")
//     @ApiResponses({
//             @ApiResponse(
//                     responseCode = "200",
//                     description = "사용자 프로필 사진 변경 성공"
//             )
//     })
//     @PatchMapping(path = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//     public BaseResponse<MemberIdResponse> modifyMyProfileAvatar(
//             @CurrentMember Member member,
//             @RequestPart(name = "avatarImage")
//             @Parameter(description = "사용자 프로필 이미지(선택 사항)") MultipartFile file
//     ) {
//         return BaseResponse.onSuccess(memberAdviser.modifyMyProfileAvatar(member, file));
//     }
// }
