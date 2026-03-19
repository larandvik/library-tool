package com.lav.libtool.service;

import com.lav.libtool.dto.bookloan.BookLoanCreateRequestDTO;
import com.lav.libtool.dto.bookloan.BookLoanResponseDTO;
import com.lav.libtool.exceptions.RemoteServiceNotAvailableException;

import java.util.List;

public interface BookLoanService {

    BookLoanResponseDTO issueBook(BookLoanCreateRequestDTO request);

    BookLoanResponseDTO returnBook(Long loanId);

    List<BookLoanResponseDTO> getLoansByReader(Long readerId);

    List<BookLoanResponseDTO> getOverdueLoans();

    List<BookLoanResponseDTO> getLoans();

    BookLoanResponseDTO getResponseFallbackIssueBook(RemoteServiceNotAvailableException e, BookLoanCreateRequestDTO request);

    BookLoanResponseDTO getResponseFallbackReturnBook(RemoteServiceNotAvailableException e, Long loanId);

}