package com.lav.libtool.exceptions;

import lombok.Getter;

@Getter
public class BookException extends RuntimeException {

    private final BookErrorType errorType;

    public BookException(BookErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }
}
