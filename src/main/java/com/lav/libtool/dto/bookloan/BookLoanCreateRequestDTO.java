package com.lav.libtool.dto.bookloan;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BookLoanCreateRequestDTO(

        @NotNull(message = "book id required")
        Long bookId,

        @NotNull(message = "reader id required")
        Long readerId,

        @NotNull(message = "due date required")
        @Future(message = "The return date must be in the future")
        LocalDate dueDate

) {}