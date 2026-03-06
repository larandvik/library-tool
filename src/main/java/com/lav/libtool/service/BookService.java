package com.lav.libtool.service;

import com.lav.libtool.dto.book.BookCreateRequestDTO;
import com.lav.libtool.dto.book.BookResponseDTO;
import com.lav.libtool.dto.book.BookUpdateRequestDTO;
import jakarta.transaction.Transactional;

import java.util.List;

public interface BookService {

    BookResponseDTO create(BookCreateRequestDTO newBook);

    BookResponseDTO findById(long id);

    List<BookResponseDTO> findAll();

    BookResponseDTO update(long id, BookUpdateRequestDTO updateBook);

    void delete(long id);

    List<BookResponseDTO> search(
            String title,
            String author,
            String isbn,
            Integer year,
            Boolean available
    );

    @Transactional
    void decreaseAvailableCopies(long bookId);

    @Transactional
    void increaseAvailableCopies(long bookId);

}
