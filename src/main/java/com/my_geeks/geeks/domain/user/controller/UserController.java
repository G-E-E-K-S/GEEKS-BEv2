package com.my_geeks.geeks.domain.user.controller;

import com.my_geeks.geeks.customResponse.BaseResponse;
import com.my_geeks.geeks.domain.user.controller.docs.UserControllerDocs;
import com.my_geeks.geeks.domain.user.requestDto.CreateUserDetailReq;
import com.my_geeks.geeks.domain.user.requestDto.LoginReq;
import com.my_geeks.geeks.domain.user.requestDto.SignUpReq;
import com.my_geeks.geeks.domain.user.requestDto.UpdateProfileReq;
import com.my_geeks.geeks.domain.user.responseDto.GetMyPageRes;
import com.my_geeks.geeks.domain.user.responseDto.GetUserDetailRes;
import com.my_geeks.geeks.domain.user.responseDto.GetUserInfoRes;
import com.my_geeks.geeks.domain.user.responseDto.GetUserProfileRes;
import com.my_geeks.geeks.domain.user.service.UserService;
import com.my_geeks.geeks.security.custom.CurrentUserId;
import com.my_geeks.geeks.security.custom.CustomUserDetails;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    // TODO: 로그인
    @PostMapping("/login")
    public BaseResponse<String> login(@RequestBody LoginReq req, HttpServletResponse response) {
        return BaseResponse.ok(userService.login(req, response));
    }

    // TODO: 로그아웃
    @PostMapping("/logout")
    public BaseResponse<String> logout(HttpServletResponse response) {
        return BaseResponse.ok(userService.logout(response));
    }

    // TODO: 생활 습관 등록
    @PostMapping("/detail/create")
    public BaseResponse<String> detailCreate(@RequestBody CreateUserDetailReq req,
                                             @CurrentUserId Long userId) {
        return BaseResponse.created(userService.createDetail(userId, req));
    }

    // TODO: 생활 습관 조회
    @GetMapping("/detail/get")
    public BaseResponse<GetUserDetailRes> detailGet(@CurrentUserId Long userId) {
        return BaseResponse.ok(userService.getUserDetail(userId));
    }

    // TODO: 생활 습관 수정
    @PutMapping("/detail/update")
    public BaseResponse<GetUserDetailRes> detailUpdate(@RequestBody CreateUserDetailReq req,
                                                       @CurrentUserId Long userId) {
        return BaseResponse.ok(userService.updateUserDetail(userId, req));
    }

    // TODO: 사용자 프로필 이미지
    @PatchMapping("/image")
    public BaseResponse<String> image(@RequestPart(value = "files", required=false) List<MultipartFile> files,
                                      @CurrentUserId Long userId) {
        return BaseResponse.ok(userService.changeImage(userId, files));
    }

    // TODO: 사용자 프로필 정보 조회
    @GetMapping("/profile")
    public BaseResponse<GetUserProfileRes> profile(@CurrentUserId Long userId) {
        return BaseResponse.ok(userService.getProfile(userId));
    }

    // TODO: 사용자 프로필 정보 수정
    @PutMapping("/profile/update")
    public BaseResponse<String> profileUpdate(
            @CurrentUserId Long userId,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestPart(value = "dto") UpdateProfileReq req
    ) {
        return BaseResponse.ok(userService.updateProfile(userId, req, files));
    }

    // TODO: 마이페이지
    @GetMapping("/mypage")
    public BaseResponse<GetMyPageRes> mypage(@CurrentUserId Long userId) {
        return BaseResponse.ok(userService.getMyPage(userId));
    }

    // TODO: 프로필 노출 변경
    @PatchMapping("/profile/change/open")
    public BaseResponse<Boolean> changeOpen(@CurrentUserId Long userId) {
        return BaseResponse.ok(userService.changeOpen(userId));
    }

   // TODO: 회원 정보 페이지
    @GetMapping("/info")
    public BaseResponse<GetUserInfoRes> userInfo(@CurrentUserId Long userId) {
        return BaseResponse.ok(userService.getUserInfo(userId));
    }

    // TODO: 쿠키 유효성 검증 & 로그인 유지
    @GetMapping("/validate")
    public BaseResponse<String> validation(@CookieValue(required = false, value = "accessToken") String accessToken) {
        return BaseResponse.ok(userService.validateCookie(accessToken));
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
