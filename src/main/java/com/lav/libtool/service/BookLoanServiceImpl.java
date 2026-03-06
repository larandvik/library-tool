package com.lav.libtool.service;

import com.lav.libtool.dto.bookloan.BookLoanCreateRequestDTO;
import com.lav.libtool.dto.bookloan.BookLoanResponseDTO;
import com.lav.libtool.entity.BookLoan;
import com.lav.libtool.entity.Status;
import com.lav.libtool.exceptions.LoanNotFoundException;
import com.lav.libtool.mappers.BookLoanMapper;
import com.lav.libtool.repository.BookLoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookLoanServiceImpl implements BookLoanService {

    private final BookLoanRepository repository;
    private final BookService bookService;
    private final ReaderService readerService;

    @Override
    public BookLoanResponseDTO issueBook(BookLoanCreateRequestDTO request) {
        var book = bookService.findById(request.bookId());
        var reader = readerService.findById(request.readerId());
        bookService.decreaseAvailableCopies(book.id());

        var savedLoan = repository.save(new BookLoan(book.id(), reader.id(), request.dueDate()));
        return BookLoanMapper.toResponseDTO(savedLoan);
    }

    @Override
    public BookLoanResponseDTO returnBook(Long loanId) {
        var loan = repository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(loanId));

        loan.markReturned();
        bookService.increaseAvailableCopies(loan.getBookId());
        return BookLoanMapper.toResponseDTO(loan);
    }

    @Override
    public List<BookLoanResponseDTO> getLoansByReader(Long readerId) {
        return repository.findByReaderId(readerId).stream()
                .map(BookLoanMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<BookLoanResponseDTO> getOverdueLoans() {
        return repository.findByStatusAndDueDateBefore(Status.ACTIVE, LocalDate.now()).stream()
                .map(BookLoanMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<BookLoanResponseDTO> getLoans() {
        return repository.findAll().stream()
                .map(BookLoanMapper::toResponseDTO)
                .toList();
    }

}
