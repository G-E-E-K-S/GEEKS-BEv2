package com.my_geeks.geeks.domain.roommate.controller;

import com.my_geeks.geeks.customResponse.BaseResponse;
import com.my_geeks.geeks.domain.roommate.controller.docs.ScheduleControllerDocs;
import com.my_geeks.geeks.domain.roommate.requestDto.CreateScheduleReq;
import com.my_geeks.geeks.domain.roommate.requestDto.UpdateScheduleReq;
import com.my_geeks.geeks.domain.roommate.responseDto.GetScheduleInfo;
import com.my_geeks.geeks.domain.roommate.responseDto.SchedulesOfDay;
import com.my_geeks.geeks.domain.roommate.service.ScheduleService;
import com.my_geeks.geeks.security.custom.CurrentUserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roommate/calendar")
public class ScheduleController implements ScheduleControllerDocs {

    private final ScheduleService scheduleService;

    // 일정 생성
    @PostMapping("/create")
    public BaseResponse<String> create(@CurrentUserId Long userId,
                               @RequestBody CreateScheduleReq req) {
        return BaseResponse.ok(scheduleService.create(userId, req));
    }

    // 전체 일정 조회
    @GetMapping("/schedules/{year}/{month}")
    public BaseResponse<List<SchedulesOfDay>> getMonthSchedule(
            @CurrentUserId Long userId,
            @PathVariable("year") int year,
            @PathVariable("month") int month
    ) {
        return BaseResponse.ok(scheduleService.getMonthSchedule(userId, year, month));
    }

    // 현재 날짜 기준 일주일치 일정 조회
    @GetMapping("/schedules/week")
    public BaseResponse<List<SchedulesOfDay>> getWeekSchedule(
            @CurrentUserId Long userId
    ) {
        return BaseResponse.ok(scheduleService.getWeekSchedule(userId));
    }

    // 일정 수정
    @PutMapping("/schedules/modify")
    public BaseResponse<String> modify(
            @CurrentUserId Long userId,
            @RequestBody UpdateScheduleReq req
            ) {
        return BaseResponse.ok(scheduleService.modify(userId, req));
    }

    // 일정 삭제
    @DeleteMapping("/schedule/delete/{roommateScheduleId}")
    public BaseResponse<String> delete(
            @CurrentUserId Long userId,
            @PathVariable("roommateScheduleId") Long roommateScheduleId
    ) {
        return BaseResponse.ok(scheduleService.delete(userId, roommateScheduleId));
    }
}
