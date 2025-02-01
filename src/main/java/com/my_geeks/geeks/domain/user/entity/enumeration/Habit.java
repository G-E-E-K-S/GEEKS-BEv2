package com.my_geeks.geeks.domain.user.entity.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Habit {
    HABIT("잠버릇 있어요"),
    NONHABIT("잠버릇 없어요");
    private final String description;
}
