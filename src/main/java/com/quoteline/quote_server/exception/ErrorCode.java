package com.quoteline.quote_server.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    REPEATED_EMAIL(HttpStatus.CONFLICT, "중복된 이메일입니다."),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 게시물을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
