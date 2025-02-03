package com.my_geeks.geeks.domain.matching.controller.docs;

import com.my_geeks.geeks.customResponse.BaseResponse;
import com.my_geeks.geeks.domain.matching.responseDto.GetPointRes;
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

import static com.my_geeks.geeks.exception.ErrorCode.DUPLICATE_EMAIL_ERROR;

@Tag(name = "Matching API", description = "룸메 찾기 관련 API")
public interface MatchingControllerDocs {
    @Operation(summary = "[룸메 찾기] 룸메 찾기 메인 탭 - 상대방의 정보와 점수 조회하는 기능",
            description = "상대방의 정보와 점수 조회하는 기능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GetPointRes.class)))
    })
    public BaseResponse<GetPointRes> points();
}
