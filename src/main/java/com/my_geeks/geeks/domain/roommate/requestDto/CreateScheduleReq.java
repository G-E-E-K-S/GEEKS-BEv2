package com.my_geeks.geeks.domain.roommate.requestDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.my_geeks.geeks.domain.roommate.entity.RoommateSchedule;
import com.my_geeks.geeks.domain.roommate.entity.enumeration.ScheduleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Schema(description = "일정 추가 DTO")
public class CreateScheduleReq {

    @Schema(description = "일정 제목", defaultValue = "외출합니다")
    private String title;

    @Schema(description = "일정 시작 날짜", defaultValue = "2025.2.20 12:00")
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.M.d HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime startDate;

    @Schema(description = "일정 종료 날짜", defaultValue = "2025.2.21 19:30")
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.M.d HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime endDate;

    @Schema(description = "일정 종류", defaultValue = "OUTING")
    private ScheduleType type;

    @Schema(description = "일정 설명", defaultValue = "외출", nullable = true)
    private String description;

    public RoommateSchedule toEntity(Long roommateId, Long writerId) {
        return RoommateSchedule.builder()
                .roommateId(roommateId)
                .writerId(writerId)
                .title(title)
                .startDate(startDate)
                .endDate(endDate)
                .type(type)
                .description(description)
                .build();
    }
}
