package com.lav.libtool.service;

import com.lav.libtool.dto.bookloan.BookLoanCreateRequestDTO;
import com.lav.libtool.dto.bookloan.BookLoanResponseDTO;
import com.lav.libtool.entity.BookLoan;
import com.lav.libtool.entity.Status;
import com.lav.libtool.exceptions.BookErrorType;
import com.lav.libtool.exceptions.BookException;
import com.lav.libtool.exceptions.RemoteServiceNotAvailableException;
import com.lav.libtool.mappers.BookLoanMapper;
import com.lav.libtool.repository.BookLoanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BookLoanServiceImpl implements BookLoanService {

    private final BookLoanRepository repository;
    private final BookService bookService;
    private final ReaderService readerService;

    @Override
    @Retryable(retryFor = {RemoteServiceNotAvailableException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000))
    public BookLoanResponseDTO issueBook(BookLoanCreateRequestDTO request) {
        log.info("Issuing book ID: {} to reader ID: {} with due date: {}",
                request.bookId(), request.readerId(), request.dueDate());

        var book = bookService.findEntityById(request.bookId());
        var reader = readerService.findEntityById(request.readerId());
        bookService.decreaseAvailableCopies(book.getId());

        var savedLoan = repository.save(new BookLoan(book, reader, request.dueDate()));
        log.info("Loan created successfully with ID: {}", savedLoan.getId());
        return BookLoanMapper.toResponseDTO(savedLoan);
    }

    @Override
    @Retryable(retryFor = {RemoteServiceNotAvailableException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000))
    public BookLoanResponseDTO returnBook(Long loanId) {
        log.info("Processing book return for loan ID: {}", loanId);

        var loan = repository.findById(loanId)
                .orElseThrow(() -> new BookException(BookErrorType.LOAN_NOT_FOUND));
        if (loan.getStatus() == Status.RETURNED) throw new BookException(BookErrorType.BOOK_ALREADY_RETURNED);

        loan.markReturned();
        bookService.increaseAvailableCopies(loan.getBook().getId());

        log.info("Book returned successfully for loan ID: {}", loanId);
        return BookLoanMapper.toResponseDTO(loan);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookLoanResponseDTO> getLoansByReader(Long readerId) {
        log.info("Fetching loans for reader ID: {}", readerId);

        return repository.findByReaderId(readerId).stream()
                .map(BookLoanMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookLoanResponseDTO> getOverdueLoans() {
        log.info("Fetching overdue loans");

        return repository.findByStatusAndDueDateBefore(Status.ACTIVE, LocalDate.now()).stream()
                .map(BookLoanMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookLoanResponseDTO> getLoans() {
        log.info("Fetching loans");

        return repository.findAll().stream()
                .map(BookLoanMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Recover
    public BookLoanResponseDTO getResponseFallbackIssueBook(RemoteServiceNotAvailableException e, BookLoanCreateRequestDTO request) {
        throw new RemoteServiceNotAvailableException();
    }

    @Override
    @Recover
    public BookLoanResponseDTO getResponseFallbackReturnBook(RemoteServiceNotAvailableException e, Long loanId) {
        throw new RemoteServiceNotAvailableException();
    }

}
