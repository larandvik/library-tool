package com.lav.libtool.exceptions;

public class ReaderNotFoundException extends RuntimeException {

    public ReaderNotFoundException(long id) {
        super("Reader with id " + id + " not found");
    }

}
