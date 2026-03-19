package com.lav.libtool.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class BookLoan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "reader_id", nullable = false)
    private Reader reader;

    @Column(nullable = false)
    private LocalDate loanDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    private LocalDate returnDate;

    @Column(nullable = false)
    private Status status = Status.ACTIVE;

    @PrePersist
    private void onCreate() {
        if (loanDate == null) {
            loanDate = LocalDate.now();
        }
    }

    public BookLoan(Book book, Reader reader, LocalDate dueDate) {
        this.book = book;
        this.reader = reader;
        this.dueDate = dueDate;
    }

    public void markReturned() {
        this.returnDate = LocalDate.now();
        this.status = Status.RETURNED;
    }

    public boolean isOverdue() {
        return status == Status.ACTIVE && dueDate.isBefore(LocalDate.now());
    }

}