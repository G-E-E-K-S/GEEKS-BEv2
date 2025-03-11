package com.my_geeks.geeks.domain.user.responseDto;

import com.my_geeks.geeks.domain.user.entity.UserDetail;
import com.my_geeks.geeks.domain.user.entity.enumeration.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 생활 습관 조회 DTO")
public class GetUserDetailRes {
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

    public static GetUserDetailRes from(UserDetail userDetail) {
        return GetUserDetailRes.builder()
                .activityTime(userDetail.getActivityTime())
                .smoke(userDetail.getSmoke())
                .ear(userDetail.getEar())
                .habit(userDetail.getHabit())
                .cleaning(userDetail.getCleaning())
                .outing(userDetail.getOuting())
                .tendency(userDetail.getTendency())
                .build();
    }
}
