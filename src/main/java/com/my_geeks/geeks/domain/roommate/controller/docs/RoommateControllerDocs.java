package com.my_geeks.geeks.domain.roommate.controller.docs;

import com.my_geeks.geeks.customResponse.BaseResponse;
import com.my_geeks.geeks.domain.roommate.requestDto.DeleteBookmarkReq;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import static com.my_geeks.geeks.exception.ErrorCode.*;

@Tag(name = "Roommate API", description = "룸메 신청 & 기능 관련 API")
public interface RoommateControllerDocs {
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
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
    @ApiErrorResponses({ALREADY_APPLY_ROOMMATE_ERROR, ALREADY_RECEIVE_APPLY_ROOMMATE_ERROR})
    public BaseResponse<String> send(Long matchingPointId, Long opponentId, Long userId);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @Operation(summary = "[마이페이지] 보낸 신청 - 내가 보낸 룸메이트 신청 목록",
            description = "내가 보낸 룸메이트 신청자들의 정보와 점수")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공 | DTO: GetApplyList",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetApplyList.class))))
    })
    public BaseResponse<List<GetApplyList>> sendList(Long userId);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @Operation(summary = "[마이페이지] 받은 신청 - 내가 받은 룸메이트 신청 목록",
            description = "내가 받은 룸메이트 신청자들의 정보와 점수")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공 | DTO: GetApplyList",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetApplyList.class))))
    })
    public BaseResponse<List<GetApplyList>> receiveList(Long userId);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
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

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
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

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
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

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @Operation(summary = "[룸메 찾기] 룸메이트 끊기",
            description = "룸메이트가 성공적으로 끊어지면 서로의 프로필이 오픈됨")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "룸메이트 끊기 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = {
                                    @ExampleObject(name = "룸메이트 끊기 성공", value = "success")
                            }))
    })
    @ApiErrorResponses({USER_NOT_FOUND})
    public BaseResponse<String> sever(Long userId);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
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
    public BaseResponse<String> bookmark(Long matchingPointId, Long opponentId, Long userId);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @Operation(summary = "[마이페이지] 룸메이트 저장 목록 - 내가 저장한 룸메이트 저장 목록",
            description = "내가 저장한 룸메이트 저장 목록 정보와 점수")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공 | DTO: GetBookmarkListRes",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetBookmarkListRes.class))))
    })
    public BaseResponse<List<GetBookmarkListRes>> bookmarkList(Long userId);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @Operation(summary = "[마이페이지] 룸메이트 저장 목록 - 내가 저장한 룸메이트 저장 단일 취소",
            description = "opponentId 보내서 신청 취소하기")
    @Parameter(name = "opponentId", description = "저장 취소할 상대방 PK", in = ParameterIn.PATH)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저장 취소 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = {
                                    @ExampleObject(name = "저장 취소 성공", value = "success")
                            }))
    })
    public BaseResponse<String> bookmarkSingleCancel(Long opponentId, Long userId);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @Operation(summary = "[마이페이지] 룸메이트 저장 목록 - 내가 저장한 룸메이트 저장 단체 취소",
            description = "GetBookmarkListRes에 있는 bookmarkId를 배열에 담아 보내서 신청 취소하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저장 취소 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = {
                                    @ExampleObject(name = "저장 취소 성공", value = "success")
                            }))
    })
    public BaseResponse<String> bookmarkBulkCancel(DeleteBookmarkReq req);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @Operation(summary = "[홈] 룸메이트 귀가 알림",
            description = "내 룸메이트에게 귀가 알림 전송")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전송 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = {
                                    @ExampleObject(name = "전송 성공", value = "success")
                            }))
    })
    @ApiErrorResponses({ROOMMATE_SERVICE_NOTIFY_NOT_ALLOW, USER_NOT_FOUND})
    public BaseResponse<String> homecomingAlarm(Long userId);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @Operation(summary = "[룸메 찾기] 룸메 신청 - 룸메이트 신청 보내기(테스트 데이터 만들기 용도)",
            description = "matchingPointId: 1 <br/>" +
                    "senderId:2<br/>" +
                    "receiverId: 1")
    @Parameter(name = "matchingPointId", description = "두 사용자의 매칭 PK", in = ParameterIn.PATH)
    @Parameter(name = "senderId", description = "신청을 보낼 사용자 PK", in = ParameterIn.PATH)
    @Parameter(name = "receiverId", description = "신청을 받는 사용자 PK", in = ParameterIn.PATH)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "신청 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = {
                                    @ExampleObject(name = "신청 성공", value = "success")
                            }))
    })
    public BaseResponse<String> send2(Long matchingPointId, Long senderId, Long receiverId);
}
