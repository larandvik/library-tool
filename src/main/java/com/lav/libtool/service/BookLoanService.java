package com.lav.libtool.service;

import com.lav.libtool.dto.bookloan.BookLoanCreateRequestDTO;
import com.lav.libtool.dto.bookloan.BookLoanResponseDTO;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Transactional
public interface BookLoanService {


    BookLoanResponseDTO issueBook(BookLoanCreateRequestDTO request);

    BookLoanResponseDTO returnBook(Long loanId);

    @Transactional(readOnly = true)
    List<BookLoanResponseDTO> getLoansByReader(Long readerId);

    @Transactional(readOnly = true)
    List<BookLoanResponseDTO> getOverdueLoans();

    @Transactional(readOnly = true)
    List<BookLoanResponseDTO> getLoans();

}
