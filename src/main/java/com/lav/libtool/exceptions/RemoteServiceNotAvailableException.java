package com.lav.libtool.exceptions;

public class RemoteServiceNotAvailableException extends RuntimeException {

    public RemoteServiceNotAvailableException() {
        super("Remote service not available");
    }

}
