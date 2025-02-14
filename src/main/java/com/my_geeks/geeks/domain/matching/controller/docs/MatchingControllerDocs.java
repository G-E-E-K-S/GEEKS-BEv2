package com.my_geeks.geeks.domain.matching.controller.docs;

import com.my_geeks.geeks.customResponse.BaseResponse;
import com.my_geeks.geeks.domain.matching.responseDto.GetMatchingDetailRes;
import com.my_geeks.geeks.domain.matching.responseDto.GetPointRes;
import com.my_geeks.geeks.swagger.annotation.ApiErrorResponse;
import com.my_geeks.geeks.swagger.annotation.ApiErrorResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;

import static com.my_geeks.geeks.exception.ErrorCode.DUPLICATE_EMAIL_ERROR;
import static com.my_geeks.geeks.exception.ErrorCode.USER_NOT_FOUND;

@Tag(name = "Matching API", description = "룸메 찾기 관련 API")
public interface MatchingControllerDocs {
    @Operation(summary = "[룸메 찾기] 룸메 찾기 메인 탭 - 상대방의 정보와 점수 조회하는 기능",
            description = "상대방의 정보와 점수 조회하는 기능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공 | DTO: GetPointRes",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GetPointRes.class)))
    })
    public BaseResponse<GetPointRes> points();

    @Operation(summary = "[홈] 룸메 찾기 홈 탭 - 상대방의 정보와 점수 상위 3개 조회하는 기능",
            description = "상대방의 정보와 점수 조회하는 기능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공 | DTO: GetPointRes",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GetPointRes.class)))
    })
    public BaseResponse<GetPointRes> pointsTop3();

    @Operation(summary = "[룸메 찾기] 상대방 정보와 점수 + 상세 생활 습관 조회 기능",
            description = "roommateStatus -> NONE(보낸 신청 없음), PENDING(내가 신청 보내고 대기중), ACCEPT(현재 룸메이트)<br/>" +
                    "bookmarkStatus -> true(저장함), false(저장 안함)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공 | DTO: GetMatchingDetailRes",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GetMatchingDetailRes.class)))
    })
    @ApiErrorResponses({USER_NOT_FOUND})
    public BaseResponse<GetMatchingDetailRes> matchingDetail(Long matchingId, Long opponentId);
}
