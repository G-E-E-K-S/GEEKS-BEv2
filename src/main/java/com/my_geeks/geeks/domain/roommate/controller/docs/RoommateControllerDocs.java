package com.my_geeks.geeks.domain.roommate.controller.docs;

import com.my_geeks.geeks.customResponse.BaseResponse;
import com.my_geeks.geeks.domain.roommate.responseDto.GetApplyList;
import com.my_geeks.geeks.domain.roommate.responseDto.GetBookmarkListRes;
import com.my_geeks.geeks.exception.ErrorCode;
import com.my_geeks.geeks.swagger.annotation.ApiErrorResponse;
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

import java.util.List;

import static com.my_geeks.geeks.exception.ErrorCode.*;

@Tag(name = "Roommate API", description = "룸메 신청 & 기능 관련 API")
public interface RoommateControllerDocs {
    @Operation(summary = "[룸메 찾기] 룸메 신청 - 룸메이트 신청 보내기",
            description = "matchingPointId와 opponentId는 룸메 찾기 상세 조회 api에서 넘겨줌")
    @Parameter(name = "opponentId", description = "신청을 보낼 사용자 PK", in = ParameterIn.PATH)
    @Parameter(name = "matchingPointId", description = "두 사용자의 매칭 PK", in = ParameterIn.PATH)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "신청 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = {
                                    @ExampleObject(name = "신청 성공", value = "success")
                            }))
    })
    public BaseResponse<String> send(Long matchingPointId, Long opponentId);

    @Operation(summary = "[마이페이지] 보낸 신청 - 내가 보낸 룸메이트 신청 목록",
            description = "내가 보낸 룸메이트 신청자들의 정보와 점수")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공 | DTO: GetApplyList",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetApplyList.class))))
    })
    public BaseResponse<List<GetApplyList>> sendList();

    @Operation(summary = "[마이페이지] 받은 신청 - 내가 받은 룸메이트 신청 목록",
            description = "내가 받은 룸메이트 신청자들의 정보와 점수")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공 | DTO: GetApplyList",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetApplyList.class))))
    })
    public BaseResponse<List<GetApplyList>> receiveList();

    @Operation(summary = "[마이페이지] 보낸 신청 - 내가 보낸 룸메이트 신청 취소하기",
            description = "GetApplyList에 있는 roommateId를 보내서 신청 취소하기")
    @Parameter(name = "roommateId", description = "룸메이트 신청 PK", in = ParameterIn.PATH)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "보낸 신청 취소 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = {
                                    @ExampleObject(name = "보낸 신청 취소 성공", value = "success")
                            }))
    })
    public BaseResponse<String> sendCancel(Long roommateId);

    @Operation(summary = "[마이페이지] 받은 신청 - 내가 받은 룸메이트 신청 거절하기",
            description = "GetApplyList에 있는 roommateId를 보내서 받은 신청 거절하기")
    @Parameter(name = "roommateId", description = "룸메이트 신청 PK", in = ParameterIn.PATH)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "받은 신청 취소 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = {
                                    @ExampleObject(name = "보낸 신청 취소 성공", value = "success")
                            }))
    })
    public BaseResponse<String> receiveRefuse(Long roommateId);

    @Operation(summary = "[마이페이지] 받은 신청 - 내가 받은 룸메이트 신청 수락하기",
            description = "GetApplyList에 있는 roommateId를 보내서 받은 신청 수락하기 <br />" +
                    "성공시 둘의 프로필을 비활성화 되어 노출되지 않고 보내고 받은 모든 신청이 삭제 <br />" +
                    "둘 중에 한명이라도 이미 누군가와 룸메이트이면 오류 발생")
    @Parameter(name = "roommateId", description = "룸메이트 신청 PK", in = ParameterIn.PATH)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "받은 신청 수락 성공 - 룸메이트 매칭",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = {
                                    @ExampleObject(name = "받은 신청 수락 성공 - 룸메이트 매칭", value = "success")
                            }))
    })
    @ApiErrorResponses({ALREADY_ACCEPT_ROOMMATE_ERROR, ROOMMATE_NOT_FOUND})
    public BaseResponse<String> receiveAccept(Long roommateId);

    @Operation(summary = "[룸메 찾기] 룸메 저장 - 룸메이트 저장하기",
            description = "matchingPointId와 opponentId는 룸메 찾기 상세 조회 api에서 넘겨줌")
    @Parameter(name = "opponentId", description = "저장할 사용자 PK", in = ParameterIn.PATH)
    @Parameter(name = "matchingPointId", description = "두 사용자의 매칭 PK", in = ParameterIn.PATH)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저장 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = {
                                    @ExampleObject(name = "저장 성공", value = "success")
                            }))
    })
    @ApiErrorResponses({ALREADY_BOOKMARK_ROOMMATE_ERROR})
    public BaseResponse<String> bookmark(Long matchingPointId, Long opponentId);

    @Operation(summary = "[마이페이지] 룸메이트 저장 목록 - 내가 저장한 룸메이트 저장 목록",
            description = "내가 저장한 룸메이트 저장 목록 정보와 점수")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공 | DTO: GetBookmarkListRes",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetBookmarkListRes.class))))
    })
    public BaseResponse<List<GetBookmarkListRes>> bookmarkList();
}
