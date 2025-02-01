package com.my_geeks.geeks.domain.user.entity.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Ear {
    BRIGHT("귀 밝아요"),
    DARK("귀 어두워요");

    private final String description;
}
