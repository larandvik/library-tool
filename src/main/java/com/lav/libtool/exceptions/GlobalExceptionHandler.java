package com.lav.libtool.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookException.class)
    public ResponseEntity<?> handle(BookException ex) {

        HttpStatus status = switch (ex.getErrorType()) {
            case BOOK_ALREADY_EXISTS,
                 BOOK_ALREADY_RETURNED,
                 BOOK_NOT_AVAILABLE,
                 READER_ALREADY_EXISTS -> HttpStatus.CONFLICT;

            case BOOK_NOT_FOUND,
                 READER_NOT_FOUND,
                 LOAN_NOT_FOUND-> HttpStatus.NOT_FOUND;
        };

        return ResponseEntity
                .status(status)
                .body(Map.of(
                        "error", ex.getErrorType().name(),
                        "message", ex.getMessage()
                ));
    }

}