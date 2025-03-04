package com.my_geeks.geeks.domain.roommate.controller.docs;

import com.my_geeks.geeks.customResponse.BaseResponse;
import com.my_geeks.geeks.domain.roommate.requestDto.CreateScheduleReq;
import com.my_geeks.geeks.domain.roommate.requestDto.UpdateScheduleReq;
import com.my_geeks.geeks.domain.roommate.responseDto.GetApplyList;
import com.my_geeks.geeks.domain.roommate.responseDto.SchedulesOfDay;
import com.my_geeks.geeks.swagger.annotation.ApiErrorResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

import static com.my_geeks.geeks.exception.ErrorCode.*;

@Tag(name = "Schedule API", description = "캘린더 기능 관련 API")
public interface ScheduleControllerDocs {
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @Operation(summary = "[캘린더] 일정 추가",
            description = "일정 추가하는 기능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "신청 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = {
                                    @ExampleObject(name = "신청 성공", value = "success")
                            }))
    })
    @ApiErrorResponses({USER_NOT_FOUND})
    public BaseResponse<String> create(Long userId, CreateScheduleReq req);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @Operation(summary = "[캘린더] 전체 일정 조회",
            description = "response.data.data 배열의 인덱스 = 일 ex) 1번 인덱스 = 2월 1일<br/>" +
                    "해당 달의 시작일부터 끝나는 일까지 각 배열에 일정이 배열로 담겨있음")
    @Parameter(name = "year", description = "조회할 일정의 연도", in = ParameterIn.PATH, example = "2025")
    @Parameter(name = "month", description = "조회할 일정의 달", in = ParameterIn.PATH, example = "2")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SchedulesOfDay.class))))
    })
    public BaseResponse<List<SchedulesOfDay>> getMonthSchedule(Long userId, int year, int month);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @Operation(summary = "[홈] 일 ~ 토 일주일치 일정 조회",
            description = "일(0) ~ 토(6)까지 일정 조회(배열 0 ~ 6번 인덱스에 일정들이 배열로 담겨있음)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SchedulesOfDay.class))))
    })
    public BaseResponse<List<SchedulesOfDay>> getWeekSchedule(Long userId);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @Operation(summary = "[캘린더] 일정 수정",
            description = "일정 수정하는 기능 || 요청 DTO: UpdateScheduleReq")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = {
                                    @ExampleObject(name = "수정 성공", value = "success")
                            }))
    })
    @ApiErrorResponses({SCHEDULE_NOT_FOUND, WRITER_NOT_MATCHED})
    public BaseResponse<String> modify(Long userId, UpdateScheduleReq req);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @Parameter(name = "roommateScheduleId", description = "삭제할 일정의 PK", in = ParameterIn.PATH)
    @Operation(summary = "[캘린더] 일정 삭제",
            description = "일정 삭제하는 기능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = {
                                    @ExampleObject(name = "삭제 성공", value = "success")
                            }))
    })
    @ApiErrorResponses({SCHEDULE_NOT_FOUND, WRITER_NOT_MATCHED})
    public BaseResponse<String> delete(Long userId, Long roommateScheduleId);
}
