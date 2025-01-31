package com.my_geeks.geeks.domain.user.controller;

import com.my_geeks.geeks.customResponse.BaseResponse;
import com.my_geeks.geeks.domain.user.controller.docs.UserControllerDocs;
import com.my_geeks.geeks.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController implements UserControllerDocs {

    private final UserService userService;

    // TODO: 이메일 중복 확인 중복 아니면 인증코드 전송
    @GetMapping("/check/email/{email}")
    public BaseResponse<Boolean> emailCheck(@PathVariable("email") String email) {
        return BaseResponse.ok(userService.emailCheck(email));
    }

    // TODO: 닉네임 중복 확인
    @GetMapping("/check/nickname/{nickname}")
    public BaseResponse<Boolean> nicknameCheck(@PathVariable("nickname") String nickname) {
        return BaseResponse.ok(userService.nicknameCheck(nickname));
    }

    // TODO: 인증코드 전송
    @GetMapping("/email/code/{email}")
    public BaseResponse<String> emailCode(@PathVariable("email") String email) {
        return BaseResponse.ok(userService.emailCode(email));
    }

    // TODO: 인증코드 확인
    @GetMapping("/auth/code/{email}/{code}")
    public BaseResponse<String> codeCheck(@PathVariable("email") String email,
                                          @PathVariable("code") String code) {
        return BaseResponse.ok(userService.checkCode(email, code));
    }

    // TODO: 회원가입
}
