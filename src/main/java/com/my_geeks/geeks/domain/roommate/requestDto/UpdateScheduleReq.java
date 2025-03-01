package com.my_geeks.geeks.domain.roommate.requestDto;

import com.my_geeks.geeks.domain.roommate.entity.enumeration.ScheduleType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UpdateScheduleReq {
    @Schema(description = "일정 PK")
    private Long roommateScheduleId;

    @Schema(description = "일정 제목")
    private String title;

    @Schema(description = "일정 시작 날짜")
    private LocalDateTime startDate;

    @Schema(description = "일정 종료 날짜")
    private LocalDateTime endDate;

    @Schema(description = "일정 종류")
    private ScheduleType type;

    @Schema(description = "일정 설명")
    private String description;
}
