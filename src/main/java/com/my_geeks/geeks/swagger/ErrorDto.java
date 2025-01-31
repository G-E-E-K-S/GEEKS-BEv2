package com.my_geeks.geeks.swagger;

import com.my_geeks.geeks.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorDto {
    private String code;
    private String message;

    static ErrorDto from(ErrorCode errorCode) {
        return new ErrorDto(errorCode.toString(), errorCode.getMessage());
    }
}
