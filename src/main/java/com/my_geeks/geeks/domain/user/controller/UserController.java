package com.my_geeks.geeks.domain.user.controller;

import com.my_geeks.geeks.customResponse.BaseResponse;
import com.my_geeks.geeks.domain.user.controller.docs.UserControllerDocs;
import com.my_geeks.geeks.domain.user.requestDto.*;
import com.my_geeks.geeks.domain.user.responseDto.*;
import com.my_geeks.geeks.domain.user.service.UserService;
import com.my_geeks.geeks.security.custom.CurrentUserId;
import com.my_geeks.geeks.security.custom.CustomUserDetails;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
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
    public BaseResponse<String> signup(@RequestBody SignUpReq req, HttpServletResponse response) {
        return BaseResponse.ok(userService.signup(req, response));
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

    // TODO: 사용자 검색
    @GetMapping("/search/{keyword}")
    public BaseResponse<List<GetUserSearchRes>> search(@CurrentUserId Long userId, @PathVariable("keyword") String keyword) {
        return BaseResponse.ok(userService.userSearch(userId, keyword));
    }

    // TODO: 사용자 fcm 토큰 받기
    @PatchMapping("/fcm/token/{token}")
    public BaseResponse<String> saveFcmToken(@CurrentUserId Long userId,
                                             @PathVariable("token") String fcmToken) {
        return BaseResponse.ok(userService.saveFcmToken(userId, fcmToken));
    }

    // TODO: 사용자 룸메이트 관련 알림 허용 여부 변경
    @PatchMapping("/change/roommate/notify")
    public BaseResponse<Boolean> changeRoommateNotify(@CurrentUserId Long userId) {
        return BaseResponse.ok(userService.changeRoommateNotify(userId));
    }

    // TODO: 사용자 서비스 관련 알림 허용 여부 변경
    @PatchMapping("/change/service/notify")
    public BaseResponse<Boolean> changeServiceNotify(@CurrentUserId Long userId) {
        return BaseResponse.ok(userService.changeServiceNotify(userId));
    }

    // TODO: 사용자 알림 허용 상태 조회
    @GetMapping ("/notify/state")
    public BaseResponse<GetNotifyStateRes> getNotifyState(@CurrentUserId Long userId) {
        return BaseResponse.ok(userService.getNotifyState(userId));
    }

    // TODO: 비밀번호 확인
    @GetMapping("/check/password/{password}")
    public BaseResponse<Boolean> checkPassword(
            @CurrentUserId Long userId,
            @PathVariable("password") String password
    ) {
        return BaseResponse.ok(userService.checkPassword(userId, password));
    }

    // TODO: 비밀번호 변경
    @PatchMapping("/change/password")
    public BaseResponse<String> changePassword(
            @CurrentUserId Long userId,
            @RequestBody UpdatePasswordReq req
            ) {
        return BaseResponse.ok(userService.changePassword(userId, req.getPassword()));
    }

    // TODO: 토큰 보유 여부 확인
    @GetMapping("/check/fcm/token")
    public BaseResponse<Boolean> checkFcmToken(@CurrentUserId Long userId) {
        return BaseResponse.ok(userService.checkFcmToken(userId));
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
