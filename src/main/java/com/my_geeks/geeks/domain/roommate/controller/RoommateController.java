package com.my_geeks.geeks.domain.roommate.controller;

import com.my_geeks.geeks.customResponse.BaseResponse;
import com.my_geeks.geeks.domain.roommate.controller.docs.RoommateControllerDocs;
import com.my_geeks.geeks.domain.roommate.responseDto.GetApplyList;
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
    public BaseResponse<List<GetApplyList>> sendList() {
        return BaseResponse.ok(roommateService.getSendList(1L));
    }

    // TODO: 받은 룸메 신청 목록
    @GetMapping("/receive/list")
    public BaseResponse<List<GetApplyList>> receiveList() {
        return BaseResponse.ok(roommateService.getReceiveList(1L));
    }

    // TODO: 보낸 룸메 신청 취소
    @DeleteMapping("/send/cancel/{roommateId}")
    public BaseResponse<String> sendCancel(@PathVariable("roommateId") Long roommateId) {
        return BaseResponse.ok(roommateService.deleteSendApply(roommateId));
    }

    // TODO: 받은 룸메 신청 거절
    @DeleteMapping("/receive/refuse/{roommateId}")
    public BaseResponse<String> receiveRefuse(@PathVariable("roommateId") Long roommateId) {
        return BaseResponse.ok(roommateService.deleteReceiveApply(roommateId));
    }

    // TODO: 받은 룸메 신청 수락하기
    @PatchMapping("/receive/accept/{roommateId}")
    public BaseResponse<String> receiveAccept(@PathVariable("roommateId") Long roommateId) {
        return BaseResponse.ok(roommateService.acceptReceiveApply(roommateId));
    }

    // TODO: 룸메 저장하기
    @PostMapping("/bookmark/{matchingPointId}/{opponentId}")
    public BaseResponse<String> bookmark(@PathVariable("matchingPointId") Long matchingPointId,
                                     @PathVariable("opponentId") Long opponentId) {
        return BaseResponse.ok(roommateService.bookmarkRoommate(1L, opponentId, matchingPointId));
    }

    // TODO: TEST 룸메 신청 보내기
    @Deprecated
    @PostMapping("/send/{matchingPointId}/{senderId}/{receiverId}")
    public BaseResponse<String> send2(@PathVariable("matchingPointId") Long matchingPointId,
                                     @PathVariable("senderId") Long senderId,
                                     @PathVariable("receiverId") Long receiverId) {
        return BaseResponse.ok(roommateService.send(senderId, receiverId, matchingPointId));
    }

}
