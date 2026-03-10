package com.lav.libtool.controllers;

import com.lav.libtool.dto.book.BookCreateRequestDTO;
import com.lav.libtool.dto.book.BookResponseDTO;
import com.lav.libtool.dto.book.BookUpdateRequestDTO;
import com.lav.libtool.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService service;

    @PostMapping
    public ResponseEntity<BookResponseDTO> create(@Valid @RequestBody BookCreateRequestDTO request) {
        log.info("POST request: create book");
        var response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getById(@PathVariable long id) {
        log.info("GET request: fetch book by id {}", id);
        var response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BookResponseDTO>> getAll() {
        log.info("GET request: fetch all books");
        var response = service.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> update(@PathVariable long id,
                                                  @Valid @RequestBody BookUpdateRequestDTO request) {
        log.info("PUT request: update book with id {}", id);
        var response = service.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        log.info("DELETE request: delete book with id {}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookResponseDTO>> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Boolean available
    ) {
        log.info("GET request: search books with title={}, author={}, isbn={}, year={}",
                title, author, isbn, year);
        return ResponseEntity.ok(service.search(title, author, isbn, year, available));
    }

}
