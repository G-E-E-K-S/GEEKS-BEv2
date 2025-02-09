package com.my_geeks.geeks.domain.matching.responseDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@Schema(description = "룸메 찾기 상세 정보")
public class GetOpponentRes {
    @Schema(description = "상대방 닉네임")
    private String nickname;

    @Schema(description = "상대방 전공")
    private String major;

    @Schema(description = "상대방 학번")
    private int studentNum;

    @Schema(description = "상대방 한줄소개")
    private String introduction;

    @Schema(description = "상대방 이미지 경로")
    private String image;

    @Schema(description = "점수")
    private int point;
}
