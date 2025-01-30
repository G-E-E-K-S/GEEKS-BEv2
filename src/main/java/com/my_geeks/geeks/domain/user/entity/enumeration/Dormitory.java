package com.my_geeks.geeks.domain.user.entity.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Dormitory {
    NEW("신관"),
    OLD("구관"),
    HAPPY("행복기숙사");

    private final String type;
}
