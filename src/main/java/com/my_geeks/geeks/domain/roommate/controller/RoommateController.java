package com.my_geeks.geeks.domain.roommate.controller;

import com.my_geeks.geeks.customResponse.BaseResponse;
import com.my_geeks.geeks.domain.roommate.controller.docs.RoommateControllerDocs;
import com.my_geeks.geeks.domain.roommate.service.RoommateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roommate")
public class RoommateController implements RoommateControllerDocs {

    private final RoommateService roommateService;

    // TODO: 룸메 신청 보내기
    @PostMapping("/send/{opponentId}")
    public BaseResponse<String> send(@PathVariable("opponentId") Long opponentId) {
        return BaseResponse.ok(roommateService.send(1L, opponentId));
    }


}
