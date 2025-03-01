package com.my_geeks.geeks.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Test Error
    TEST_ERROR(10000, HttpStatus.BAD_REQUEST, "테스트 에러입니다."),
    // 404 Not Found
    NOT_FOUND_END_POINT(40400, HttpStatus.NOT_FOUND, "존재하지 않는 API입니다."),
    USER_NOT_FOUND(40401, HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    ROOMMATE_NOT_FOUND(40402, HttpStatus.NOT_FOUND, "존재하지 않는 룸메이트 신청입니다."),
    EMAIL_NOT_FOUND(40403, HttpStatus.NOT_FOUND, "위 이메일로 가입된 정보가 없어요"),
    PASSWORD_NOT_ALLOWED(40404, HttpStatus.NOT_FOUND, "비밀번호를 다시 확인해 주세요"),
    // Duplicate Data Error
    DUPLICATE_DATA_ERROR(40900, HttpStatus.CONFLICT, "중복 데이터 오류입니다."),
    DUPLICATE_EMAIL_ERROR(40901, HttpStatus.CONFLICT, "이미 가입된 이메일입니다."),
    DUPLICATE_NICKNAME_ERROR(40902, HttpStatus.CONFLICT, "이미 가입된 닉네임입니다."),
    // JWT Filter Error
    JWT_FILTER_ERROR(40301, HttpStatus.FORBIDDEN, "JWT filter 인증 오류 접근 권한이 없거나 토큰이 존재 하지 않습니다."),
    JWT_INVALID_TOKEN_ERROR(40110, HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 입니다."),
    JWT_EXPIRED_TOKEN_ERROR(40111, HttpStatus.UNAUTHORIZED, "JWT 유효시간 만료입니다."),
    JWT_UNSUPPORTED_TOKEN_ERROR(40112, HttpStatus.UNAUTHORIZED, "지원하지 않는 JWT 형식입니다."),
    JWT_CLAIMS_EMPTY_ERROR(40113, HttpStatus.UNAUTHORIZED, "JWT Claim 접근 오류입니다."),
    INVALID_CODE_ERROR(50030, HttpStatus.INTERNAL_SERVER_ERROR, "시간이 만료되었거나, 유효하지 않은 인증코드입니다."),
    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR(50000, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),
    ALREADY_ACCEPT_ROOMMATE_ERROR(50020, HttpStatus.INTERNAL_SERVER_ERROR, "이미 매칭이 성사된 사용자가 있습니다."),
    ALREADY_BOOKMARK_ROOMMATE_ERROR(50021, HttpStatus.INTERNAL_SERVER_ERROR, "이미 저장된 사용자입니다."),
    ALREADY_APPLY_ROOMMATE_ERROR(50022, HttpStatus.INTERNAL_SERVER_ERROR, "이미 룸메이트를 신청한 사용자입니다."),
    ALREADY_RECEIVE_APPLY_ROOMMATE_ERROR(50023, HttpStatus.INTERNAL_SERVER_ERROR, "상대방이 이미 룸메이트를 신청했습니다."),
    ROOMMATE_SERVICE_NOTIFY_NOT_ALLOW(50024, HttpStatus.INTERNAL_SERVER_ERROR, "룸메이트가 서비스 알림을 허용하지 않았습니다."),
    SCHEDULE_NOT_FOUND(50025, HttpStatus.INTERNAL_SERVER_ERROR, "존재하지 않는 일정입니다."),
    WRITER_NOT_MATCHED(50025, HttpStatus.INTERNAL_SERVER_ERROR, "일정의 작성가자 일치하지 않습니다."),
    // JSON 파싱 오류
    JSON_PARSING_ERROR(50010, HttpStatus.INTERNAL_SERVER_ERROR, "JSON 파싱 오류입니다."),
    // firebase 초기화 오류
    FIREBASE_CONFIG_ERROR(50090, HttpStatus.INTERNAL_SERVER_ERROR, "firebase 초기화 오류입니다."),
    // firebase message 생성 오류
    FIREBASE_MESSAGE_ERROR(50091, HttpStatus.INTERNAL_SERVER_ERROR, "firebase message 생성 오류입니다."),
    // Discord Message JSON 오류
    DISCORD_CONVERT_JSON_ERROR(50092, HttpStatus.INTERNAL_SERVER_ERROR, "Discord Convert JSON 오류입니다."),
    // Discord Webhook 전송 오류
    DISCORD_WEBHOOK_ERROR(50093, HttpStatus.INTERNAL_SERVER_ERROR, "Discord Webhook 전송 오류입니다."),

    //S3 이미지 업로드 오류
    AWS_S3_UPLOAD_ERROR(50030, HttpStatus.INTERNAL_SERVER_ERROR, "AWS S3 이미지 업로드 실패 오류입니다.");
    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;
}
