package com.my_geeks.geeks.domain.user.controller.docs;

import com.my_geeks.geeks.customResponse.BaseResponse;
import com.my_geeks.geeks.exception.ErrorCode;
import com.my_geeks.geeks.security.custom.CustomUserDetails;
import com.my_geeks.geeks.swagger.annotation.ApiErrorResponse;
import com.my_geeks.geeks.swagger.annotation.ApiErrorResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User API", description = "사용자 관련 API")
public interface UserControllerDocs {
    @Operation(summary = "[온보딩] 이메일 중복 확인",
            description = "false가 반환되면 사용 가능한 이메일, true가 반환되면 이미 가입된 이메일")
    @Parameter(name = "email", description = "사용자가 입력한 이메일", in = ParameterIn.PATH)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 중복 여부",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class),
                            examples = {
                                    @ExampleObject(name = "이메일 중복 O", value = "true"),
                                    @ExampleObject(name = "이메일 중복 X", value = "false")
                            })),
    })
    public BaseResponse<Boolean> emailCheck(String email);

    @Operation(summary = "[온보딩] 닉네임 중복 확인",
            description = "false가 반환되면 사용 가능한 닉네임, true가 반환되면 이미 가입된 닉네임")
    @Parameter(name = "email", description = "사용자가 입력한 이메일", in = ParameterIn.PATH)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "닉네임 중복 여부",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class),
                            examples = {
                                    @ExampleObject(name = "닉네임 중복 O", value = "true"),
                                    @ExampleObject(name = "닉네임 중복 X", value = "false")
                            })),
    })
    public BaseResponse<Boolean> nicknameCheck(String email);
}
