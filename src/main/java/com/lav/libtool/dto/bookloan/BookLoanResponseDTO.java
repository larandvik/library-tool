package com.lav.libtool.dto.bookloan;

import com.lav.libtool.entity.Status;

import java.time.LocalDate;

public record BookLoanResponseDTO (

        Long id,
        Long bookId,
        Long readerId,
        LocalDate loanDate,
        LocalDate dueDate,
        Status status,
        LocalDate returnDate,
        Boolean isOverdue

){}