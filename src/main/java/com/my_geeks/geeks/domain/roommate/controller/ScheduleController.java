package com.my_geeks.geeks.domain.roommate.controller;

import com.my_geeks.geeks.customResponse.BaseResponse;
import com.my_geeks.geeks.domain.roommate.controller.docs.ScheduleControllerDocs;
import com.my_geeks.geeks.domain.roommate.requestDto.CreateScheduleReq;
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

    @PostMapping("/create")
    public BaseResponse<String> create(@CurrentUserId Long userId,
                               @RequestBody CreateScheduleReq req) {
        return BaseResponse.ok(scheduleService.create(userId, req));
    }

    @GetMapping("/schedules/{year}/{month}")
    public BaseResponse<List<SchedulesOfDay>> getMonthSchedule(
            @CurrentUserId Long userId,
            @PathVariable("year") int year,
            @PathVariable("month") int month
    ) {
        return BaseResponse.ok(scheduleService.getMonthSchedule(userId, year, month));
    }

}
