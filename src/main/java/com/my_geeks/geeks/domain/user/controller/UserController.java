package com.my_geeks.geeks.domain.user.controller;

import com.my_geeks.geeks.customResponse.BaseResponse;
import com.my_geeks.geeks.domain.user.controller.docs.UserControllerDocs;
import com.my_geeks.geeks.domain.user.requestDto.CreateUserDetailReq;
import com.my_geeks.geeks.domain.user.requestDto.SignUpReq;
import com.my_geeks.geeks.domain.user.responseDto.GetUserDetailRes;
import com.my_geeks.geeks.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController implements UserControllerDocs {

    private final UserService userService;

    // TODO: 이메일 중복 확인 중복 아니면 인증코드 전송
    @GetMapping("/check/email/{email}")
    public BaseResponse<String> emailCheck(@PathVariable("email") String email) {
        return BaseResponse.ok(userService.emailCheck(email));
    }

    // TODO: 인증코드 확인
    @GetMapping("/auth/code/{email}/{code}")
    public BaseResponse<String> codeCheck(@PathVariable("email") String email,
                                          @PathVariable("code") String code) {
        return BaseResponse.ok(userService.checkCode(email, code));
    }

    // TODO: 닉네임 중복 확인
    @GetMapping("/check/nickname/{nickname}")
    public BaseResponse<String> nicknameCheck(@PathVariable("nickname") String nickname) {
        return BaseResponse.ok(userService.nicknameCheck(nickname));
    }

    // TODO: 회원가입
    @PostMapping("/signup")
    public BaseResponse<String> signup(@RequestBody SignUpReq req) {
        return BaseResponse.ok(userService.signup(req));
    }

    // TODO: 생활 습관 등록
    @PostMapping("/detail/create")
    public BaseResponse<String> detailCreate(@RequestBody CreateUserDetailReq req) {
        return BaseResponse.created(userService.createDetail(1L, req));
    }

    // TODO: 생활 습관 조회
    @GetMapping("/detail/get")
    public BaseResponse<GetUserDetailRes> detailGet() {
        return BaseResponse.ok(userService.getUserDetail(1L));
    }

    // TODO: 생활 습관 등록 TEST
    @Deprecated
    @PostMapping("/test/create/{userId}")
    public BaseResponse<String> testCreate(@PathVariable("userId") Long userId, @RequestBody CreateUserDetailReq req) {
        return BaseResponse.created(userService.createDetail(userId, req));
    }

    @Deprecated
    @PostMapping("/total/create")
    public BaseResponse<String> totalCreate(@RequestBody CreateUserDetailReq req) {
        return BaseResponse.created(userService.createTotalDetail(req));
    }

    @Deprecated
    @GetMapping("/healthy")
    public String healthy() {
        return "success";
    }
}
