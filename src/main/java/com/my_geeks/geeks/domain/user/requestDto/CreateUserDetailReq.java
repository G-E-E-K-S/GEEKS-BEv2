package com.my_geeks.geeks.domain.user.requestDto;

import com.my_geeks.geeks.domain.user.entity.UserDetail;
import com.my_geeks.geeks.domain.user.entity.enumeration.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "사용자 생활 습관 등록 DTO")
public class CreateUserDetailReq {
    @Schema(description = "흡연", defaultValue = "NONSMOKER")
    private Smoke smoke;

    @Schema(description = "잠버릇", defaultValue = "NONHABIT")
    private Habit habit;

    @Schema(description = "잠귀", defaultValue = "DARK")
    private Ear ear;

    @Schema(description = "활동 시간", defaultValue = "MORNING")
    private ActivityTime activityTime;

    @Schema(description = "외출", defaultValue = "INSIDE")
    private Outing outing;

    @Schema(description = "청소", defaultValue = "CLEAN")
    private Cleaning cleaning;

    @Schema(description = "성향", defaultValue = "ALONE")
    private Tendency tendency;

    public UserDetail toEntity(Long userId) {
        return UserDetail.builder()
                .id(userId)
                .smoke(smoke)
                .habit(habit)
                .ear(ear)
                .activityTime(activityTime)
                .outing(outing)
                .cleaning(cleaning)
                .tendency(tendency)
                .build();
    }
}
