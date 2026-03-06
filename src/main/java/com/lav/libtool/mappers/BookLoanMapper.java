package com.lav.libtool.mappers;

import com.lav.libtool.dto.bookloan.BookLoanResponseDTO;
import com.lav.libtool.entity.BookLoan;

public final class BookLoanMapper {

    private BookLoanMapper() {
    }

    public static BookLoanResponseDTO toResponseDTO(BookLoan bookLoan) {
        return new BookLoanResponseDTO(
                bookLoan.getId(),
                bookLoan.getBookId(),
                bookLoan.getReaderId(),
                bookLoan.getLoanDate(),
                bookLoan.getDueDate(),
                bookLoan.getStatus(),
                bookLoan.getReturnDate(),
                bookLoan.isOverdue()
        );
    }

}
