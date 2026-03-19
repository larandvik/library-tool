package com.lav.libtool.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BookErrorType {

    BOOK_ALREADY_EXISTS("Book already exists"),
    BOOK_ALREADY_RETURNED("Book already returned"),
    BOOK_NOT_AVAILABLE("Book not available"),
    BOOK_NOT_FOUND("Book not found"),

    READER_ALREADY_EXISTS("Reader already exists"),
    READER_NOT_FOUND("Reader not found"),

    LOAN_NOT_FOUND("Loan not found");

    private final String message;

}