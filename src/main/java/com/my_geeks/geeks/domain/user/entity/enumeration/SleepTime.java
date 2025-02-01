package com.my_geeks.geeks.domain.user.entity.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SleepTime {
    EARLY("일찍 자요"),
    LATE("늦게 자요"),
    SOMETIMES("때마다 달라요");

    private final String description;
}
