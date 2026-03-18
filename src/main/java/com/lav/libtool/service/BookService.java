package com.lav.libtool.service;

import com.lav.libtool.dto.book.BookCreateRequestDTO;
import com.lav.libtool.dto.book.BookResponseDTO;
import com.lav.libtool.dto.book.BookUpdateRequestDTO;
import com.lav.libtool.entity.Book;

import java.util.List;

public interface BookService {

    BookResponseDTO create(BookCreateRequestDTO newBook);

    BookResponseDTO findById(long id);

    Book findEntityById(Long id);

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

    void decreaseAvailableCopies(long bookId);

    void increaseAvailableCopies(long bookId);

}
