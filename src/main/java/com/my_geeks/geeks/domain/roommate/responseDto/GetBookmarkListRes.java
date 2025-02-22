package com.my_geeks.geeks.domain.roommate.responseDto;

import com.my_geeks.geeks.domain.user.entity.enumeration.Smoke;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Schema(description = "룸메이트 저장 목록")
public class GetBookmarkListRes {
    @Schema(description = "룸메이트 저장 PK")
    private Long bookmarkId;

    @Schema(description = "상대방 PK")
    private Long opponentId;

    @Schema(description = "매칭 PK")
    private Long matchingPointId;

    @Schema(description = "상대방 닉네임")
    private String nickname;

    @Schema(description = "상대방 전공")
    private String major;

    @Schema(description = "상대방 학번")
    private int studentNum;

    @Schema(description = "상대방 프로필 이미지 - 설정 하지 않았으면 null")
    private String image;

    @Schema(description = "상대방 흡연 여부")
    private Smoke smoke;

    @Schema(description = "점수")
    private int point;

    @Schema(description = "신청 보낸 날짜")
    private LocalDateTime createdDate;
}
