package com.lav.libtool.dto.book;

public record BookResponseDTO(

        Long id,
        String title,
        String author,
        String isbn,
        Integer publicationYear,
        Integer totalCopies,
        Integer availableCopies

) {}
