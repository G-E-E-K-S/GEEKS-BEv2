package com.my_geeks.geeks.domain.matching.responseDto;

import com.my_geeks.geeks.domain.user.responseDto.GetUserDetailRes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GetMatchingDetailRes {
    private GetOpponentRes opponent;

    private GetUserDetailRes myDetail;

    private GetUserDetailRes opponentDetail;
}
