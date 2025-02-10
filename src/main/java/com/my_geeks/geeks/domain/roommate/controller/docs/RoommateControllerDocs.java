package com.my_geeks.geeks.domain.roommate.controller.docs;

import com.my_geeks.geeks.customResponse.BaseResponse;
import com.my_geeks.geeks.domain.matching.responseDto.GetPointRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Roommate API", description = "룸메 신청 & 기능 관련 API")
public interface RoommateControllerDocs {
    @Operation(summary = "[룸메 찾기] 룸메 신청 - 룸메이트 신청 보내기",
            description = "룸메이트 신청 보내기")
    @Parameter(name = "opponentId", description = "신청을 보낼 사용자 PK", in = ParameterIn.PATH)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "신청 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = {
                                    @ExampleObject(name = "신청 성공", value = "success")
                            }))
    })
    public BaseResponse<String> send(Long opponentId);
}
