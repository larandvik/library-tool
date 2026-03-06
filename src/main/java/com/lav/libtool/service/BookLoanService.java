package com.lav.libtool.service;

import com.lav.libtool.dto.bookloan.BookLoanCreateRequestDTO;
import com.lav.libtool.dto.bookloan.BookLoanResponseDTO;
import com.lav.libtool.exceptions.RemoteServiceNotAvailableException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface BookLoanService {

    @Retryable(retryFor = {RemoteServiceNotAvailableException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000))
    BookLoanResponseDTO issueBook(BookLoanCreateRequestDTO request);

    @Retryable(retryFor = {RemoteServiceNotAvailableException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000))
    BookLoanResponseDTO returnBook(Long loanId);

    @Transactional(readOnly = true)
    List<BookLoanResponseDTO> getLoansByReader(Long readerId);

    @Transactional(readOnly = true)
    List<BookLoanResponseDTO> getOverdueLoans();

    @Transactional(readOnly = true)
    List<BookLoanResponseDTO> getLoans();

    @Recover
    BookLoanResponseDTO getResponseFallbackIssueBook(RemoteServiceNotAvailableException e, BookLoanCreateRequestDTO request);

    @Recover
    BookLoanResponseDTO getResponseFallbackReturnBook(RemoteServiceNotAvailableException e, Long loanId);

}