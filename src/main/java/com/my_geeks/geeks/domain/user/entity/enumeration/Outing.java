package com.my_geeks.geeks.domain.user.entity.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Outing {
    INSIDE("집에 있는 걸 좋아해요"),
    OUTSIDE("나가는 걸 좋아해요");
    private final String description;
}
