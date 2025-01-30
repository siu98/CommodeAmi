package com.siuuuuu.commodeami.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// 에러 상태 별 메세지
@Getter
@AllArgsConstructor
public enum ErrorCode {

    //400
    WRONG_ENTRY_POINT(40000, HttpStatus.BAD_REQUEST, "잘못된 접근입니다"),
    MISSING_REQUEST_PARAMETER(40001, HttpStatus.BAD_REQUEST, "필수 요청 파라미터가 누락되었습니다."),
    INVALID_PARAMETER_FORMAT(40002, HttpStatus.BAD_REQUEST, "요청에 유효하지 않은 인자 형식입니다."),
    BAD_REQUEST_JSON(40003, HttpStatus.BAD_REQUEST, "잘못된 JSON 형식입니다."),
    // 데이터 무결성 위반 추가(ex: db의 NOT NULL 속성)
    DATA_INTEGRITY_VIOLATION(40005, HttpStatus.BAD_REQUEST,
            "데이터 무결성 위반입니다. 필수 값이 누락되었거나 유효하지 않습니다."),
    INVALID_INPUT_VALUE(40010, HttpStatus.BAD_REQUEST, "잘못된 입력 값입니다."),
    INVALID_REQUEST_BODY(40011, HttpStatus.BAD_REQUEST, "잘못된 요청 본문입니다."),
    MISSING_REQUIRED_FIELD(40012, HttpStatus.BAD_REQUEST, "필수 필드가 누락되었습니다."),
    EXIST_USER_ID(40013, HttpStatus.BAD_REQUEST, "이미 존재하는 회원입니다."),
    INVALID_PASSWORD(40014, HttpStatus.BAD_REQUEST, "현재 비밀번호가 일치하지 않습니다."),
    EXIST_PASSWORD(40015, HttpStatus.BAD_REQUEST, "현재 비밀번호와 일치합니다."),
    INVALID_PASSWORD_PATTERN(40016, HttpStatus.BAD_REQUEST, "비밀번호는 영문자, 숫자, 특수문자를 포함해야 합니다."),
    INVALID_PASSWORD_LENGTH(40017, HttpStatus.BAD_REQUEST, "최소 8자 이상 20자 이하로 구성해야합니다."),
    INVALID_PASSWORD_REPEAT(40018, HttpStatus.BAD_REQUEST, "동일한 문자를 3번 이상 연속해서 사용할 수 없습니다."),
    INVALID_REPEAT_SCHEDULE_START_END(40021,HttpStatus.BAD_REQUEST,"일정 시작일보다 종료일이 앞설 수 없습니다."),
    INVALID_VERIFICATION_CODE(40022, HttpStatus.BAD_REQUEST, "잘못된 인증번호입니다. 인증번호를 다시 확인해주세요"),

    //401
    INVALID_HEADER_VALUE(40100, HttpStatus.UNAUTHORIZED, "올바르지 않은 헤더값입니다."),
    EXPIRED_TOKEN_ERROR(40101, HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_TOKEN_ERROR(40102, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    TOKEN_MALFORMED_ERROR(40103, HttpStatus.UNAUTHORIZED, "토큰이 올바르지 않습니다."),
    TOKEN_TYPE_ERROR(40104, HttpStatus.UNAUTHORIZED, "토큰 타입이 일치하지 않거나 비어있습니다."),
    TOKEN_UNSUPPORTED_ERROR(40105, HttpStatus.UNAUTHORIZED, "지원하지않는 토큰입니다."),
    TOKEN_GENERATION_ERROR(40106, HttpStatus.UNAUTHORIZED, "토큰 생성에 실패하였습니다."),
    TOKEN_UNKNOWN_ERROR(40107, HttpStatus.UNAUTHORIZED, "알 수 없는 토큰입니다."),
    LOGIN_FAILURE(40108, HttpStatus.UNAUTHORIZED, "로그인에 실패했습니다"),
    UNAUTHORIZED_ACCESS(40110, HttpStatus.UNAUTHORIZED, "인증되지 않은 접근입니다."),
    EXPIRED_SESSION(40111, HttpStatus.UNAUTHORIZED, "세션이 만료되었습니다."),
    ACCESS_DENIED(40312, HttpStatus.FORBIDDEN, "접근이 거부되었습니다."),
    LOGOUT_ACCESS_TOKEN(400113, HttpStatus.BAD_REQUEST, "로그아웃된 accessToken입니다."),
    NOT_FOUND_REFRESH_TOKEN(40314, HttpStatus.UNAUTHORIZED, "refreshToken이 존재하지 않습니다."),
    NOT_FOUND_COOKIE(40315, HttpStatus.BAD_REQUEST, "쿠키가 없습니다."),
    DUPLICATE_EMAIL_EXISTS(4010015,HttpStatus.UNAUTHORIZED,"이미 존재하는 이메일입니다."),

    //403
    FORBIDDEN_ROLE(40300, HttpStatus.FORBIDDEN, "권한이 존재하지 않습니다."),

    //404
    NOT_FOUND_USER(40401, HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다.");



    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;
}
