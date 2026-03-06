package com.lav.libtool.controllers;

import com.lav.libtool.dto.bookloan.BookLoanCreateRequestDTO;
import com.lav.libtool.dto.bookloan.BookLoanResponseDTO;
import com.lav.libtool.service.BookLoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class BookLoanController {

    private final BookLoanService service;

    @PostMapping
    public ResponseEntity<BookLoanResponseDTO> issueBook(@Valid @RequestBody BookLoanCreateRequestDTO request) {
        var response = service.issueBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<BookLoanResponseDTO> returnBook(@PathVariable long id) {
        var response = service.returnBook(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reader/{readerId}")
    public ResponseEntity<List<BookLoanResponseDTO>> readerHistory(@PathVariable long readerId) {
        var response = service.getLoansByReader(readerId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<BookLoanResponseDTO>> overdueBooks() {
        var response = service.getOverdueLoans();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BookLoanResponseDTO>> bookLoans() {
        var response = service.getLoans();
        return ResponseEntity.ok(response);
    }

}
