package com.lav.libtool.exceptions;

public class ReaderAlreadyExistsException extends RuntimeException {

    public ReaderAlreadyExistsException(String email) {
        super("Reader with email " + email + " already exists");
    }

}
