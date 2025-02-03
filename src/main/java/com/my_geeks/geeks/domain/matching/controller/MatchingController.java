package com.my_geeks.geeks.domain.matching.controller;

import com.my_geeks.geeks.customResponse.BaseResponse;
import com.my_geeks.geeks.domain.matching.controller.docs.MatchingControllerDocs;
import com.my_geeks.geeks.domain.matching.responseDto.GetPointRes;
import com.my_geeks.geeks.domain.matching.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/matching")
public class MatchingController implements MatchingControllerDocs {
    private final MatchingService matchingService;

    @GetMapping("/points")
    public BaseResponse<GetPointRes> points() {
        return BaseResponse.ok(matchingService.get(1L));
    }
}
