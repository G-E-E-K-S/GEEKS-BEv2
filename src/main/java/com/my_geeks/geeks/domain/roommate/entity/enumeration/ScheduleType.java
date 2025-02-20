package com.my_geeks.geeks.domain.roommate.entity.enumeration;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ScheduleType {
    OUTING("외출"),
    SLEEPOVER("외박"),
    TOGETHER("공동일정"),
    ETC("기타");
    private final String description;
}
