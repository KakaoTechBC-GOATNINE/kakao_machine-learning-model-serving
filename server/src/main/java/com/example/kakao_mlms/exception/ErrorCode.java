package com.example.kakao_mlms.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Not Found Error
    NOT_FOUND_USER("4040", HttpStatus.NOT_FOUND, "해당 사용자가 존재하지 않습니다."),
    NOT_FOUND_QNA("4041", HttpStatus.NOT_FOUND, "해당 Qna가 존재하지 않습니다."),
    NOT_FOUND_END_POINT("4042", HttpStatus.NOT_FOUND, "존재하지 않는 API 엔드포인트입니다."),
    // Server, File Up/DownLoad Error
    SERVER_ERROR("5000", HttpStatus.INTERNAL_SERVER_ERROR, "API 서버 오류입니다."),

    // Access Denied Error
    ACCESS_DENIED_ERROR("4030", HttpStatus.FORBIDDEN, "액세스 권한이 없습니다."),

    // Bad Request Error
    NOT_END_POINT("4000", HttpStatus.BAD_REQUEST, "End Point 가 존재하지 않습니다."),
    INVALID_PARAMETER("4001", HttpStatus.BAD_REQUEST, "Invalid PARAMETER"),
    INVALID_HEADER("4002", HttpStatus.BAD_REQUEST, "Invalid Header"),
    BAD_DATA("4003", HttpStatus.BAD_REQUEST, "데이터가 올바르지 않습니다."),
    BAD_REQUEST_JSON("4004", HttpStatus.BAD_REQUEST, "잘못된 JSON 형식입니다."),
    INVALID_PARAMETER_FORMAT("4005", HttpStatus.BAD_REQUEST, "요청에 유효하지 않은 파라미터 형식입니다."),
    MISSING_REQUEST_PARAMETER("4006", HttpStatus.BAD_REQUEST, "필수 요청 파라미터가 누락되었습니다."),
    DUPLICATION_USER("4007", HttpStatus.BAD_REQUEST, "해당 유저가 이미 있습니다"),
    DUPLICATION_IDORNICKNAME("4008", HttpStatus.BAD_REQUEST, "해당 아이디 혹은 닉네임이 이미 존재합니다"),
    /**
     * 502 Bad Gateway: Gateway Server Error
     */
    AUTH_SERVER_USER_INFO_ERROR("5020", HttpStatus.BAD_GATEWAY, "소셜 서버에서 유저 정보를 가져오는데 실패하였습니다."),

    /**
     * 401 Unauthorized: Authentication and Authorization Error
     */
    EXPIRED_TOKEN_ERROR("4010", HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_TOKEN_ERROR("4011", HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    FAILURE_LOGIN("4012", HttpStatus.UNAUTHORIZED, "로그인에 실패했습니다."),
    TOKEN_MALFORMED_ERROR("4013", HttpStatus.UNAUTHORIZED, "토큰이 올바르지 않습니다."),
    TOKEN_TYPE_ERROR("4014", HttpStatus.UNAUTHORIZED, "토큰 타입이 일치하지 않습니다."),
    TOKEN_UNSUPPORTED_ERROR("4015", HttpStatus.UNAUTHORIZED, "지원하지않는 토큰입니다."),
    TOKEN_UNKNOWN_ERROR("4016", HttpStatus.UNAUTHORIZED, "알 수 없는 토큰입니다.");


    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
