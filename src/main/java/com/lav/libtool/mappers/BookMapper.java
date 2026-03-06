package com.lav.libtool.mappers;

import com.lav.libtool.dto.book.BookResponseDTO;
import com.lav.libtool.entity.Book;


public final class BookMapper {

    private BookMapper() {}

    public static BookResponseDTO toResponseDTO(Book book) {
        return new BookResponseDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPublicationYear(),
                book.getTotalCopies(),
                book.getAvailableCopies()
        );
    }

}
