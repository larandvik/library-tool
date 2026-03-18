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

        var book = bookService.findEntityById(request.bookId());
        var reader = readerService.findEntityById(request.readerId());
        bookService.decreaseAvailableCopies(book.getId());

        var savedLoan = repository.save(new BookLoan(book, reader, request.dueDate()));
        log.info("Loan created successfully with ID: {}", savedLoan.getId());
        return BookLoanMapper.toResponseDTO(savedLoan);
    }

    @Override
    // TODO: Самая крупная проблема тут
    //  1) Я бы не делал readOnly для метода возврата книги,
    //  так как там есть изменения в сущности и вызов другого сервиса, который тоже изменяет данные.
    //  Spring Boot по умолчанию использует класс‑based proxy (CGLIB),
    //  поэтому аннотации на интерфейсе в большинстве конфигураций игнорируются,
    //  а фактическая транзакция берётся из BookLoanServiceImpl.
    //  Получается, что returnBook() → открывает read‑only транзакцию.
    //  Внутри неё вызывается bookService.increaseAvailableCopies(...) → она участвует в той же read‑only транзакции
    //  Потому что у тебя по умолч propagation = REQUIRED.
    //  Провайдер JPA может:
    //      вообще не делать flush изменений,
    //      или даже бросать исключение при попытке записи (зависит от реализации).
    //  Поэтому: читать в readOnly=true можно, писать — нельзя/небезопасно.
    //  Здесь лучше просто оставить класс @Transactional
    //  2) можно специфицировать rollBackFor, если нужно откатывать транзакцию при определенных исключениях
    @Transactional(readOnly = true)
    public BookLoanResponseDTO returnBook(Long loanId) {
        log.info("Processing book return for loan ID: {}", loanId);

        var loan = repository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(loanId));

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
    public BookLoanResponseDTO getResponseFallbackIssueBook(RemoteServiceNotAvailableException e, BookLoanCreateRequestDTO request) {
        throw new RemoteServiceNotAvailableException();
    }

    @Override
    public BookLoanResponseDTO getResponseFallbackReturnBook(RemoteServiceNotAvailableException e, Long loanId) {
        throw new RemoteServiceNotAvailableException();
    }

}
