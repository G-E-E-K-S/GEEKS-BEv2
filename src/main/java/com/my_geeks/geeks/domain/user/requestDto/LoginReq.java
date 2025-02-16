package com.my_geeks.geeks.domain.user.requestDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginReq {
    @Schema(description = "로그인 할 이메일", defaultValue = "bak3839@naver.com")
    private String email;

    @Schema(description = "비밀번호", defaultValue = "1234")
    private String password;
}
