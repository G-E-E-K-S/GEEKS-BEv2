package com.my_geeks.geeks.domain.user.entity.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Tendency {
    ALONE("혼자 조용히 지내요"),
    TOGETHER("함께 놀고 싶어요"),
    OPPONENT("상대에게 맞춰요");

    private final String description;
}
