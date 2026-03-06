package com.lav.libtool.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(unique = true, nullable = false, length = 13)
    private String isbn;

    private int publicationYear;

    @Column(nullable = false)
    private int totalCopies;

    @Column(nullable = false)
    private int availableCopies;

    public Book(String title, String author, String isbn, int publicationYear, int totalCopies) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    public void updateDetails(String title, String author, Integer publishYear, Integer totalCopies) {

        if (title != null) {
            this.title = title;
        }

        if (author != null) {
            this.author = author;
        }

        if (publishYear != null) {
            this.publicationYear = publishYear;
        }

        if (totalCopies != null) {
            this.totalCopies = totalCopies;
            this.availableCopies = Math.min(this.availableCopies, this.totalCopies);
        }
    }

    public void borrowCopy() {
        availableCopies--;
    }

    public void returnCopy() {
        availableCopies++;
    }

}
