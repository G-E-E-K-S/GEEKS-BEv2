package com.my_geeks.geeks.customResponse;

import com.my_geeks.geeks.exception.CustomException;
import com.my_geeks.geeks.exception.ExceptionDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

@Getter
@NoArgsConstructor
public class BaseResponse<T> {
        private HttpStatus httpStatus;
        private boolean success;
        @Nullable private T data;
        @Nullable private ExceptionDto error;

        private BaseResponse(HttpStatus httpStatus, boolean success, @Nullable T data, @Nullable ExceptionDto error) {
                this.httpStatus = httpStatus;
                this.success = success;
                this.data = data;
                this.error = error;
        }

        public static <T> BaseResponse<T> ok(@Nullable final T data) {
                return new BaseResponse<>(HttpStatus.OK, true, data, null);
        }

        public static <T> BaseResponse<T> created(@Nullable final T data) {
                return new BaseResponse<>(HttpStatus.CREATED, true, data, null);
        }

        public static <T> BaseResponse<T> fail(final CustomException e) {
                return new BaseResponse<>(e.getErrorCode().getHttpStatus(), false, null, ExceptionDto.of(e.getErrorCode()));
        }
}