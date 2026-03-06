package com.lav.libtool.dto.book;

import jakarta.validation.constraints.PositiveOrZero;

public record BookUpdateRequestDTO(
        String title,
        String author,
        Integer publicationYear,
        @PositiveOrZero
        Integer totalCopies
) {}
