package com.lav.libtool.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record BookUpdateRequestDTO(

        @NotBlank
        String title,

        @NotBlank
        String author,

        @NotNull
        @PositiveOrZero
        Integer publicationYear,

        @NotNull
        @PositiveOrZero
        Integer totalCopies

) {}