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
        if (this.status == Status.RETURNED) {
            // TODO: 1) У тебя есть кастомное исключение, но ты его не используешь, поэтому у тебя будет 500 вместо ожидаемого обработчика
            //  2) Я бы сделал общую ошибку BookEXception или BookLoanException для всех типов искл этого домена с типом ошибки BOOK_ALREADY_RETURNED
            //  3) и не надо харкодить текст ошибки, лучше использовать константы или enum для типов ошибок
            // TODO:4) Тут такое же замечание как в Reader: я бы не стал делать проверку на статус в самом методе сущности, а просто позволил бы сервису контролировать логику возврата
            throw new IllegalStateException("Book already returned");
        }

        this.returnDate = LocalDate.now();
        this.status = Status.RETURNED;
    }

    public boolean isOverdue() {
        return status == Status.ACTIVE && dueDate.isBefore(LocalDate.now());
    }

}