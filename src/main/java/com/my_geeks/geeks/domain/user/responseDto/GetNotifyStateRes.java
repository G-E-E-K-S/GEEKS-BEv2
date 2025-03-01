package com.my_geeks.geeks.domain.user.responseDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "사용자 알림 허용 상태 조회 DTO")
public class GetNotifyStateRes {

    @Schema(description = "룸메이트 알림")
    private boolean roommate;

    @Schema(description = "채팅 알림")
    private boolean chat;

    @Schema(description = "서비스 알림")
    private boolean service;

    @Schema(description = "마케팅 알림")
    private boolean marketing;
}
