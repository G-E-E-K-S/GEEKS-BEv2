package com.my_geeks.geeks.domain.matching.responseDto;

import com.my_geeks.geeks.domain.roommate.entity.enumeration.RoommateStatus;
import com.my_geeks.geeks.domain.user.responseDto.GetUserDetailRes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GetMatchingDetailRes {
    private RoommateStatus roommateStatus;

    private boolean bookmarkStatus;

    private GetOpponentRes opponent;

    private GetUserDetailRes myDetail;

    private GetUserDetailRes opponentDetail;
}
