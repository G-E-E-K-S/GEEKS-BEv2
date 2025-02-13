package com.my_geeks.geeks.domain.roommate.requestDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "룸메이트 저장 취소")
public class DeleteBookmarkReq {
    @Schema(description = "저장 취소할 bookmarkId를 담은 배열")
    private List<Long> bookmarkIds;
}
