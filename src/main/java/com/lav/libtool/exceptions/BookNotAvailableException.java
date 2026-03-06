package com.lav.libtool.exceptions;

import com.lav.libtool.dto.book.BookResponseDTO;

public class BookNotAvailableException extends RuntimeException {

    public BookNotAvailableException(BookResponseDTO book) {
        super("No available copies of the book " + book);
    }

}
