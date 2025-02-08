package com.my_geeks.geeks.domain.matching.responseDto;

import com.my_geeks.geeks.domain.user.entity.enumeration.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class UserDetailAndPoint {
    private Long matchingPointId;

    private Long otherId;

    private String smoke;

    private String habit;

    private String ear;

    private String activityTime;

    private String outing;

    private String cleaning;

    private String tendency;

    private int point;
}
