package com.lav.libtool.service;

import com.lav.libtool.dto.book.BookCreateRequestDTO;
import com.lav.libtool.dto.book.BookResponseDTO;
import com.lav.libtool.dto.book.BookUpdateRequestDTO;
import com.lav.libtool.entity.Book;
import com.lav.libtool.exceptions.BookAlreadyExistsException;
import com.lav.libtool.exceptions.BookNotAvailableException;
import com.lav.libtool.exceptions.BookNotFoundException;
import com.lav.libtool.mappers.BookMapper;
import com.lav.libtool.repository.BookRepository;
import com.lav.libtool.spec.BookSpecifications;
import com.lav.libtool.util.NormalizerIsbn;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository repository;

    @Override
    public BookResponseDTO create(BookCreateRequestDTO newBook) {
        var normalizeIsbn = NormalizerIsbn.normalize(newBook.isbn());

        if(repository.existsByIsbn(normalizeIsbn)){
            throw new BookAlreadyExistsException(newBook.isbn());
        }

        Book book = new Book(newBook.title(),
                newBook.author(),
                normalizeIsbn,
                newBook.publicationYear(),
                newBook.totalCopies());
        var savedBook = repository.save(book);

        return BookMapper.toResponseDTO(savedBook);
    }

    @Override
    public BookResponseDTO findById(long id) {
        return repository.findById(id)
                .map(BookMapper::toResponseDTO)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @Override
    public List<BookResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(BookMapper::toResponseDTO)
                .toList();
    }

    @Override
    public BookResponseDTO update(long id, BookUpdateRequestDTO updateBook) {
        var book = repository.findById(id).orElseThrow(() -> new BookNotFoundException(id));

        book.updateDetails(updateBook.title(), updateBook.author(), updateBook.publicationYear(), updateBook.totalCopies());
        var savedBook = repository.save(book);
        return BookMapper.toResponseDTO(savedBook);
    }

    @Override
    public void delete(long id) {
        if(!repository.existsById(id)){
            throw new BookNotFoundException(id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<BookResponseDTO> search(String title,
                                        String author,
                                        String isbn,
                                        Integer year,
                                        Boolean available) {

        Specification<Book> spec = Specification.unrestricted();

        if (title != null && !title.isBlank()) {
            spec = spec.and(BookSpecifications.titleContains(title));
        }

        if (author != null && !author.isBlank()) {
            spec = spec.and(BookSpecifications.authorContains(author));
        }

        if (isbn != null && !isbn.isBlank()) {
            String normalized = NormalizerIsbn.normalize(isbn);
            spec = spec.and(BookSpecifications.isbnEquals(normalized));
        }

        if (year != null) {
            spec = spec.and(BookSpecifications.publicationYearEquals(year));
        }

        if (available != null) {
            spec = spec.and(BookSpecifications.availableEquals(available));
        }

        return repository.findAll(spec).stream().map(BookMapper::toResponseDTO).toList();
    }

    @Override
    public void decreaseAvailableCopies(long bookId) {
        Book book = repository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        if (book.getAvailableCopies() == 0) {
            throw new BookNotAvailableException(BookMapper.toResponseDTO(book));
        }
        book.borrowCopy();
    }

    @Override
    public void increaseAvailableCopies(long bookId) {
        Book book = repository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        book.returnCopy();
    }

}
