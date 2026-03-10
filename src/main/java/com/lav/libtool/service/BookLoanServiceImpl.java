package com.lav.libtool.service;

import com.lav.libtool.dto.bookloan.BookLoanCreateRequestDTO;
import com.lav.libtool.dto.bookloan.BookLoanResponseDTO;
import com.lav.libtool.entity.BookLoan;
import com.lav.libtool.entity.Status;
import com.lav.libtool.exceptions.LoanNotFoundException;
import com.lav.libtool.exceptions.RemoteServiceNotAvailableException;
import com.lav.libtool.mappers.BookLoanMapper;
import com.lav.libtool.repository.BookLoanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public BookLoanResponseDTO issueBook(BookLoanCreateRequestDTO request) {
        log.info("Issuing book ID: {} to reader ID: {} with due date: {}",
                request.bookId(), request.readerId(), request.dueDate());

        var book = bookService.findById(request.bookId());
        var reader = readerService.findById(request.readerId());
        bookService.decreaseAvailableCopies(book.id());

        var savedLoan = repository.save(new BookLoan(book.id(), reader.id(), request.dueDate()));
        log.info("Loan created successfully with ID: {}", savedLoan.getId());
        return BookLoanMapper.toResponseDTO(savedLoan);
    }

    @Override
    @Transactional(readOnly = true)
    public BookLoanResponseDTO returnBook(Long loanId) {
        log.info("Processing book return for loan ID: {}", loanId);

        var loan = repository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(loanId));

        loan.markReturned();
        bookService.increaseAvailableCopies(loan.getBookId());

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
    public BookLoanResponseDTO getResponseFallbackIssueBook(RemoteServiceNotAvailableException e, BookLoanCreateRequestDTO request) {
        throw new RemoteServiceNotAvailableException();
    }

    @Override
    public BookLoanResponseDTO getResponseFallbackReturnBook(RemoteServiceNotAvailableException e, Long loanId) {
        throw new RemoteServiceNotAvailableException();
    }

}
