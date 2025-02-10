package com.my_geeks.geeks.domain.roommate.controller;

import com.my_geeks.geeks.customResponse.BaseResponse;
import com.my_geeks.geeks.domain.roommate.controller.docs.RoommateControllerDocs;
import com.my_geeks.geeks.domain.roommate.responseDto.GetSendList;
import com.my_geeks.geeks.domain.roommate.service.RoommateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roommate")
public class RoommateController implements RoommateControllerDocs {

    private final RoommateService roommateService;

    // TODO: 룸메 신청 보내기
    @PostMapping("/send/{matchingPointId}/{opponentId}")
    public BaseResponse<String> send(@PathVariable("matchingPointId") Long matchingPointId,
                                     @PathVariable("opponentId") Long opponentId) {
        return BaseResponse.ok(roommateService.send(1L, opponentId, matchingPointId));
    }

    // TODO: 보낸 룸메 신청 목록
    @GetMapping("/send/list")
    public BaseResponse<List<GetSendList>> sendList() {
        return BaseResponse.ok(roommateService.getSendList(1L));
    }

    // TODO: 받은 룸메 신청 목록
}
