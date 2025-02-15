package com.my_geeks.geeks.domain.user.responseDto;

import com.my_geeks.geeks.domain.user.entity.User;
import com.my_geeks.geeks.domain.user.entity.UserDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "마이페이지 사용자 정보 + 내 룸메이트")
public class GetMyPageRes {
    @Schema(description = "사용자 닉네임")
    private String nickname;

    @Schema(description = "사용자 전공")
    private String major;

    @Schema(description = "사용자 학번")
    private int studentNum;

    @Schema(description = "사용자 한 줄 소개")
    private String introduction;

    @Schema(description = "사용자 프로필 이미지 이름")
    private String image;

    @Schema(description = "사용자 프로필 노출 여부")
    private boolean isOpen;

    @Schema(description = "사용자 룸메이트 정보")
    private MyRoommate myRoommate;

    @Getter
    @Builder
    @AllArgsConstructor
    @Schema(description = "룸메이트 정보")
    public static class MyRoommate {
        @Schema(description = "상대방 PK")
        private Long opponentId;

        @Schema(description = "상대방 닉네임")
        private String nickname;

        @Schema(description = "상대방 전공")
        private String major;

        @Schema(description = "상대방 학번")
        private int studentNum;

        @Schema(description = "상대방 프로필 이미지 - 설정 하지 않았으면 null")
        private String image;
    }

    public static GetMyPageRes from(User user, User userRoommate) {
        GetMyPageRes res = new GetMyPageRes();

        res.nickname = user.getNickname();
        res.major = user.getMajor();
        res.studentNum = user.getStudentNum();
        res.introduction = user.getIntroduction();
        res.image = user.getImage();
        res.isOpen = user.isOpen();

        if(userRoommate != null) {
            res.myRoommate = MyRoommate.builder()
                    .image(userRoommate.getImage())
                    .major(userRoommate.getMajor())
                    .nickname(userRoommate.getNickname())
                    .opponentId(userRoommate.getId())
                    .studentNum(userRoommate.getStudentNum())
                    .build();
        }
        else res.myRoommate = null;

        return res;
    }


}
