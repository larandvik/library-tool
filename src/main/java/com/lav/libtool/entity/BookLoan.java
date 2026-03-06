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

    @Column(nullable = false)
    private Long bookId;

    @Column(nullable = false)
    private Long readerId;

    @Column(nullable = false)
    private final LocalDate loanDate = LocalDate.now();

    @Column(nullable = false)
    private LocalDate dueDate;

    private LocalDate returnDate;

    @Column(nullable = false)
    private Status status = Status.ACTIVE;

    public BookLoan(Long bookId, Long readerId, LocalDate dueDate) {
        this.bookId = bookId;
        this.readerId = readerId;
        this.dueDate = dueDate;
    }

    public void markReturned() {
        if (this.status == Status.RETURNED) {
            throw new IllegalStateException("Book already returned");
        }

        this.returnDate = LocalDate.now();
        this.status = Status.RETURNED;
    }

    public boolean isOverdue() {
        return status == Status.ACTIVE && dueDate.isBefore(LocalDate.now());
    }

}