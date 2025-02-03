package com.my_geeks.geeks.domain.user.entity.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Cleaning {
    CLEAN("주기적으로 청소해요"),
    DIRTY("더러워지면 청소해요");
    private final String description;
}
