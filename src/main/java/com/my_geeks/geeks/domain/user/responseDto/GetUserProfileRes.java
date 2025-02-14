package com.my_geeks.geeks.domain.user.responseDto;


import com.my_geeks.geeks.domain.user.entity.User;
import com.my_geeks.geeks.domain.user.entity.enumeration.Dormitory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "사용자 프로필 정보 조회 DTO")
public class GetUserProfileRes {
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

    @Schema(description = "사용자 프로필 이미지 이름")
    private String image;

    public static GetUserProfileRes from(User user) {
        GetUserProfileRes res = new GetUserProfileRes();

        res.nickname = user.getNickname();
        res.major = user.getMajor();
        res.studentNum = user.getStudentNum();
        res.dormitory = user.getDormitory();
        res.introduction = user.getIntroduction();
        res.image = user.getImage();

        return res;
    }
}
