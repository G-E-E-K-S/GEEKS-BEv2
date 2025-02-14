package com.my_geeks.geeks.domain.user.requestDto;

import com.my_geeks.geeks.domain.user.entity.enumeration.Dormitory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "사용자 프로필 정보 변경 DTO")
public class UpdateProfileReq {
    @Schema(description = "사용자 닉네임")
    private String nickname;

    @Schema(description = "사용자 전공")
    private String major;

    @Schema(description = "사용자 학번")
    private int studentNum;

    @Schema(description = "사용자 기숙사")
    private Dormitory dormitory;

    @Schema(description = "사용자 한 줄 소개")
    private String introduction;
}
