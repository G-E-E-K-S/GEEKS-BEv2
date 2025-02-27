package com.my_geeks.geeks.domain.roommate.responseDto;

import com.my_geeks.geeks.domain.roommate.entity.enumeration.ScheduleType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GetScheduleInfo {
    @Schema(description = "일정 PK")
    private Long roommateScheduleId;

    @Schema(description = "일정 제목")
    private String title;

    @Schema(description = "일정 시작 날짜")
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.M.d HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime startDate;

    @Schema(description = "일정 종료 날짜")
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.M.d HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime endDate;

    @Schema(description = "일정 종류")
    private ScheduleType type;

    @Schema(description = "일정 설명")
    private String description;

    @Schema(description = "일정 추가한 사용자 닉네임")
    private String nickname;

    @Schema(description = "일정 사용자가 작성자인지 확인하는 변수(true이면 작성자)")
    private boolean writerStatus;
}
