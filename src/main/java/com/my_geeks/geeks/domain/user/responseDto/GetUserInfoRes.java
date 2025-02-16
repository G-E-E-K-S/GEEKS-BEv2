package com.my_geeks.geeks.domain.user.responseDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "회원 정보 설정 페이지 DTO")
public class GetUserInfoRes {

    @Schema(description = "사용자 이메일")
    private String email;

    @Schema(description = "사용자 가입 날짜")
    private LocalDateTime createdDate;
}
