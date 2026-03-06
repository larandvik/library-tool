package com.lav.libtool.exceptions;

public class BookAlreadyReturnedException extends RuntimeException {

    public BookAlreadyReturnedException() {
        super("Book already returned");
    }

}
