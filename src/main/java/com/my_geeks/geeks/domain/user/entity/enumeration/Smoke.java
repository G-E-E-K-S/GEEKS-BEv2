package com.my_geeks.geeks.domain.user.entity.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Smoke {
    SMOKER("흡연자에요"),
    NONSMOKER("비흡연자에요");

    private final String description;
}
