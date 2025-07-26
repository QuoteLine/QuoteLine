package com.quoteline.quote_server.exception;

import lombok.Getter;

@Getter
public class QuoteLineException extends RuntimeException {

    private final ErrorCode errorCode;
    public QuoteLineException(final ErrorCode errorCode, String details) {
        super(details);
        this.errorCode = errorCode;
    }
}
