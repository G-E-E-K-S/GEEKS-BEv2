package com.my_geeks.geeks.domain.user.entity.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Outing {
    INSIDE("집순이에요"),
    OUTSIDE("밖순이에요"),
    HOME("본가 자주 가요"),
    PROMISE("약속이 있으면 나가요"),
    SCHOOL("학교에 오래 있어요");
    private final String description;
}
