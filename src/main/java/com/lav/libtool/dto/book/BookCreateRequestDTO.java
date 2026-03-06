package com.lav.libtool.dto.book;

import jakarta.validation.constraints.*;

public record BookCreateRequestDTO(
        @NotBlank(message = "title must not be blank")
        String title,

        @NotBlank(message = "author must not be blank")
        String author,

        @NotBlank(message = "ISBN is required")
        @Pattern(regexp = "^(?:\\d{9}[\\dX]|\\d{13})$",
                message = "ISBN must be 10 or 13 characters")
        String isbn,

        @NotNull(message = "Year is required")
        @Min(value = 1, message = "year must be valid")
        @Max(value = 9999, message = "year must be valid")
        Integer publicationYear,

        @NotNull(message = "total copies is required")
        @PositiveOrZero(message = "total copies must be zero or positive")
        Integer totalCopies
) {}
