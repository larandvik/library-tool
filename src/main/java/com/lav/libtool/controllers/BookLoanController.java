package com.lav.libtool.controllers;

import com.lav.libtool.dto.bookloan.BookLoanCreateRequestDTO;
import com.lav.libtool.dto.bookloan.BookLoanResponseDTO;
import com.lav.libtool.service.BookLoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class BookLoanController {

    private final BookLoanService service;

    @PostMapping
    public ResponseEntity<BookLoanResponseDTO> issueBook(@Valid @RequestBody BookLoanCreateRequestDTO request) {
        log.info("POST request: issue loan for book {} to reader {} with due date {}",
                request.bookId(), request.readerId(), request.dueDate());
        var response = service.issueBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<BookLoanResponseDTO> returnBook(@PathVariable long id) {
        log.info("PUT request: return book for loan {}", id);
        var response = service.returnBook(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reader/{readerId}")
    public ResponseEntity<List<BookLoanResponseDTO>> getReaderHistory(@PathVariable long readerId) {
        log.info("GET request: fetch complete history for reader {}", readerId);
        var response = service.getLoansByReader(readerId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<BookLoanResponseDTO>> getOverdueLoans() {
        log.info("GET request: fetch overdue loans");
        var response = service.getOverdueLoans();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BookLoanResponseDTO>> getAllLoans() {

        var response = service.getLoans();
        return ResponseEntity.ok(response);
    }

}
