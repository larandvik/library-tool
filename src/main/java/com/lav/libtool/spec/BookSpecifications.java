package com.lav.libtool.spec;

import com.lav.libtool.entity.Book;
import org.springframework.data.jpa.domain.Specification;

public final class BookSpecifications {

    private BookSpecifications() {}

    public static Specification<Book> titleContains(String title) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("title")),
                "%" + title.toLowerCase() + "%");
    }

    public static Specification<Book> authorContains(String author) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("author")),
                "%" + author.toLowerCase() + "%");
    }

    public static Specification<Book> isbnEquals(String isbn) {
        return (root, query, cb) -> cb.equal(root.get("isbn"), isbn);
    }

    public static Specification<Book> publicationYearEquals(Integer publicationYear) {
        return (root, query, cb) -> cb.equal(root.get("publicationYear"), publicationYear);
    }

    public static Specification<Book> availableEquals(Boolean available) {
        return (root, query, cb) -> available
                ? cb.greaterThan(root.get("availableCopies"), 0)
                : cb.equal(root.get("availableCopies"), 0);
    }

}
