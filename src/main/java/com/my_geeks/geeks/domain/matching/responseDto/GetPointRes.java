package com.my_geeks.geeks.domain.matching.responseDto;

import com.my_geeks.geeks.domain.user.entity.enumeration.Smoke;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "룸메 찾기 페이지 상대방 정보 + 나와의 점수 DTO")
public class GetPointRes {

    @Schema(description = "사용자의 생활 습관이 등록 여부(true면 생활 습관 등록 완료)", defaultValue = "false")
    private boolean exists;

    @Schema(description = "사용자와 성별 & 기숙사 종류가 같은 사용자들")
    private List<OpponentInfo> opponentInfos;

    @Getter
    @Builder
    @AllArgsConstructor
    @Schema(description = "상대방 정보")
    public static class OpponentInfo {
        @Schema(description = "매칭 PK")
        private Long matchingId;

        @Schema(description = "상대방 PK")
        private Long opponentId;

        @Schema(description = "상대방 닉네임")
        private String nickname;

        @Schema(description = "상대방 전공")
        private String major;

        @Schema(description = "상대방 학번")
        private int studentNum;

        @Schema(description = "상대방 한줄 소개 - 설정 하지 않았으면 null")
        private String introduction;

        @Schema(description = "상대방 흡연 여부")
        private String smoke;

        @Schema(description = "상대방 프로필 이미지 - 설정 하지 않았으면 null")
        private String image;

        @Schema(description = "점수")
        private int point;
    }
}
