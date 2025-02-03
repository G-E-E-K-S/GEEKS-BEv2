package com.my_geeks.geeks.domain.user.entity.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActivityTime {
    MORNING("아침형"),
    DAWN("새벽형");
    private final String description;
}
