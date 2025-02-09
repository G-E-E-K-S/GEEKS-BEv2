package com.my_geeks.geeks.domain.matching.controller;

import com.my_geeks.geeks.customResponse.BaseResponse;
import com.my_geeks.geeks.domain.matching.controller.docs.MatchingControllerDocs;
import com.my_geeks.geeks.domain.matching.responseDto.GetMatchingDetailRes;
import com.my_geeks.geeks.domain.matching.responseDto.GetPointRes;
import com.my_geeks.geeks.domain.matching.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/matching")
public class MatchingController implements MatchingControllerDocs {
    private final MatchingService matchingService;

    // TODO: 룸메 찾기 - 상대방 정보와 점수
    @GetMapping("/points")
    public BaseResponse<GetPointRes> points() {
        return BaseResponse.ok(matchingService.getPoints(1L));
    }

    // TODO: 홈 - 상대방 정보와 점수 상위 3개
    @GetMapping("/points/top3")
    public BaseResponse<GetPointRes> pointsTop3() {
        return BaseResponse.ok(matchingService.getPointsTop3(1L));
    }

    // TODO: 룸메 찾기 상세 정보
    @GetMapping("/detail/{matchingId}/{opponentId}")
    public BaseResponse<GetMatchingDetailRes> matchingDetail(@PathVariable("matchingId") Long matchingId,
                                                             @PathVariable("opponentId") Long opponentId) {
        return BaseResponse.ok(matchingService.getMatchingDetail(1L, opponentId, matchingId));
    }
}
