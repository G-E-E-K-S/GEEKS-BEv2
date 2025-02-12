package com.my_geeks.geeks.domain.roommate.entity.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoommateStatus {
    PENDING("대기"),
    ACCEPT("수락");

    private final String description;
}
