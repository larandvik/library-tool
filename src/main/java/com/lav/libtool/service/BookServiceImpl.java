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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository repository;

    @Override
    public BookResponseDTO create(BookCreateRequestDTO newBook) {
        log.info("Creating new book: {}", newBook.title());

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

        log.info("Book created successfully with ID: {}", savedBook.getId());
        return BookMapper.toResponseDTO(savedBook);
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponseDTO findById(long id) {
        log.debug("Fetching book with ID: {}", id);

        return repository.findById(id)
                .map(BookMapper::toResponseDTO)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Book findEntityById(Long id) {
        log.debug("Fetching book with ID: {}", id);

        return repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDTO> findAll() {
        log.debug("Fetching all books");

        return repository.findAll().stream()
                .map(BookMapper::toResponseDTO)
                .toList();
    }

    @Override
    public BookResponseDTO update(long id, BookUpdateRequestDTO updateBook) {
        log.info("Updating book with ID: {}", id);

        var book = repository.findById(id).orElseThrow(() -> new BookNotFoundException(id));

        book.updateDetails(updateBook.title(), updateBook.author(), updateBook.publicationYear(), updateBook.totalCopies());
        var savedBook = repository.save(book);

        log.info("Book updated successfully with ID: {}", id);
        return BookMapper.toResponseDTO(savedBook);
    }

    @Override
    public void delete(long id) {
        log.info("Deleting book with ID: {}", id);

        if(!repository.existsById(id)){
            throw new BookNotFoundException(id);
        }
        repository.deleteById(id);
        log.info("Book deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
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
        Book book = findEntityById(bookId);

        log.debug("Decreasing available copies for book ID: {}", bookId);

        if (book.getAvailableCopies() == 0) {
            throw new BookNotAvailableException(BookMapper.toResponseDTO(book));
        }
        book.borrowCopy();
    }

    @Override
    public void increaseAvailableCopies(long bookId) {
        Book book = findEntityById(bookId);

        log.debug("Increasing available copies for book ID: {}", bookId);

        book.returnCopy();
    }

}
