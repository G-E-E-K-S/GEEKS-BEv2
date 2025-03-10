package com.my_geeks.geeks.domain.user.entity.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotifyType {
    HOMECOMING("귀가"),
    ROOMMATE_NEW_APPLY("새로운 룸메이트 신청"),
    ROOMMATE_MATCHING_SUCCESS("룸메이트 매칭 성공"),
    ROOMMATE_MATCHING_FAIL("룸메이트 매칭 실패"),
    ROOMMATE_SEVER("룸메이트 끊김");

    private final String description;
}
