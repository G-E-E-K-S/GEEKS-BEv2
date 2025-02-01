package com.my_geeks.geeks.domain.user.controller.docs;

import com.my_geeks.geeks.customResponse.BaseResponse;
import com.my_geeks.geeks.domain.user.requestDto.SignUpReq;
import com.my_geeks.geeks.exception.ErrorCode;
import com.my_geeks.geeks.security.custom.CustomUserDetails;
import com.my_geeks.geeks.swagger.annotation.ApiErrorResponse;
import com.my_geeks.geeks.swagger.annotation.ApiErrorResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User API", description = "사용자 관련 API")
public interface UserControllerDocs {
    @Operation(summary = "[온보딩] 이메일 중복 확인 & 인증코드 발송",
            description = "사용가능한 이메일이면 available 반환과 함께 해당 이메일로 인증코드 발송, 중복시 오류 코드 반환")
    @Parameter(name = "email", description = "사용자가 입력한 이메일", in = ParameterIn.PATH)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 사용가능",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = {
                                    @ExampleObject(name = "이메일 사용 가능", value = "available")
                            })),
    })
    @ApiErrorResponses({ErrorCode.DUPLICATE_EMAIL_ERROR})
    public BaseResponse<String> emailCheck(String email);

    @Operation(summary = "[온보딩] 인증코드 검증",
            description = "해당 이메일로 전송된 인증코드 검증 실패시 오류 코드 반환")
    @Parameters(value = {
            @Parameter(name = "email", description = "사용자가 입력한 이메일", in = ParameterIn.PATH),
            @Parameter(name = "code", description = "사용자가 입력한 인증코드", in = ParameterIn.PATH)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = {
                                    @ExampleObject(name = "인증 성공", value = "success")
                            })),
    })
    @ApiErrorResponses({ErrorCode.INVALID_CODE_ERROR})
    public BaseResponse<String> codeCheck(String email, String code);

    @Operation(summary = "[온보딩] 닉네임 중복 확인",
            description = "사용가능한 닉네임이면 available 반환, 중복시 오류 코드 반환")
    @Parameter(name = "email", description = "사용자가 입력한 이메일", in = ParameterIn.PATH)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "닉네임 사용가능",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = {
                                    @ExampleObject(name = "닉네임 사용 가능", value = "true")
                            })),
    })
    @ApiErrorResponses({ErrorCode.DUPLICATE_NICKNAME_ERROR})
    public BaseResponse<String> nicknameCheck(String email);

    @Operation(summary = "[온보딩] 회원가입",
            description = "회원가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = {
                                    @ExampleObject(name = "회원가입 성공", value = "success")
                            })),
    })
    public BaseResponse<String> signup(SignUpReq req);
}
