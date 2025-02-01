package com.my_geeks.geeks.domain.user.entity.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WakeupTime {
    EARLY("일찍 일어나요"),
    LATE("늦게 일어나요"),
    SOMETIMES("때마다 달라요");
    private final String description;
}
