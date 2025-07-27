package com.quoteline.quote_server.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(QuoteLineException.class)
    public ResponseEntity<ErrorResponse> handleQuoteLine(QuoteLineException ex) {
        ErrorCode code = ex.getErrorCode();
        return ResponseEntity
                .status(code.getStatus())
                .body(new ErrorResponse(code.getMessage()));
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(org.springframework.dao.DataIntegrityViolationException ex) {
        return ResponseEntity
                .status(ErrorCode.REPEATED_EMAIL.getStatus())
                .body(new ErrorResponse(ErrorCode.REPEATED_EMAIL.getMessage()));
    }
}
