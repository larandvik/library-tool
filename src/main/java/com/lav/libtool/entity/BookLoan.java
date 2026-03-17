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

    // TODO: BookLoan хранит bookId/readerId как простые поля вместо связей JPA 
    @Column(nullable = false)
    private Long bookId;

    @Column(nullable = false)
    private Long readerId;

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

    public BookLoan(Long bookId, Long readerId, LocalDate dueDate) {
        this.bookId = bookId;
        this.readerId = readerId;
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