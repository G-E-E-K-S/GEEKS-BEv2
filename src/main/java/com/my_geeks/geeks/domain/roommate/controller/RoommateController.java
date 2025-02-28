package com.my_geeks.geeks.domain.roommate.controller;

import com.my_geeks.geeks.customResponse.BaseResponse;
import com.my_geeks.geeks.domain.roommate.controller.docs.RoommateControllerDocs;
import com.my_geeks.geeks.domain.roommate.requestDto.DeleteBookmarkReq;
import com.my_geeks.geeks.domain.roommate.responseDto.GetApplyList;
import com.my_geeks.geeks.domain.roommate.responseDto.GetBookmarkListRes;
import com.my_geeks.geeks.domain.roommate.service.RoommateService;
import com.my_geeks.geeks.security.custom.CurrentUserId;
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
                                     @PathVariable("opponentId") Long opponentId,
                                     @CurrentUserId Long userId) {
        return BaseResponse.ok(roommateService.send(userId, opponentId, matchingPointId));
    }

    // TODO: 보낸 룸메 신청 목록
    @GetMapping("/send/list")
    public BaseResponse<List<GetApplyList>> sendList(@CurrentUserId Long userId) {
        return BaseResponse.ok(roommateService.getSendList(userId));
    }

    // TODO: 받은 룸메 신청 목록
    @GetMapping("/receive/list")
    public BaseResponse<List<GetApplyList>> receiveList(@CurrentUserId Long userId) {
        return BaseResponse.ok(roommateService.getReceiveList(userId));
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

    // TODO: 룸메 끊기
    @DeleteMapping("/sever")
    public BaseResponse<String> sever(@CurrentUserId Long userId) {
        return BaseResponse.ok(roommateService.roommateSever(userId));
    }

    // TODO: 룸메 저장하기
    @PostMapping("/bookmark/{matchingPointId}/{opponentId}")
    public BaseResponse<String> bookmark(@PathVariable("matchingPointId") Long matchingPointId,
                                     @PathVariable("opponentId") Long opponentId,
                                         @CurrentUserId Long userId) {
        return BaseResponse.ok(roommateService.bookmarkRoommate(userId, opponentId, matchingPointId));
    }

    // TODO: 룸메 저장 목록
    @GetMapping("/bookmark/list")
    public BaseResponse<List<GetBookmarkListRes>> bookmarkList(@CurrentUserId Long userId) {
        return BaseResponse.ok(roommateService.getBookmarkList(userId));
    }

    // TODO: 룸메 저장 단일 삭제
    @DeleteMapping("/bookmark/cancel/{opponentId}")
    public BaseResponse<String> bookmarkSingleCancel(@PathVariable("opponentId") Long opponentId,
                                                     @CurrentUserId Long userId) {
        return BaseResponse.ok(roommateService.deleteSingleBookmark(userId, opponentId));
    }

    // TODO: 룸메 저장 벌크 삭제
    @DeleteMapping("/bookmark/cancel")
    public BaseResponse<String> bookmarkBulkCancel(@RequestBody DeleteBookmarkReq req) {
        return BaseResponse.ok(roommateService.deleteBulkBookmark(req));
    }

    // TODO: 룸메 귀가 알림 보내기
    @PostMapping("/homecoming")
    public BaseResponse<String> homecomingAlarm(@CurrentUserId Long userId) {
        return BaseResponse.ok(roommateService.homecomingAlarm(userId));
    }

    // TODO: TEST 룸메 신청 보내기
    @PostMapping("/send/{matchingPointId}/{senderId}/{receiverId}")
    public BaseResponse<String> send2(@PathVariable("matchingPointId") Long matchingPointId,
                                     @PathVariable("senderId") Long senderId,
                                     @PathVariable("receiverId") Long receiverId) {
        return BaseResponse.ok(roommateService.send(senderId, receiverId, matchingPointId));
    }

}
