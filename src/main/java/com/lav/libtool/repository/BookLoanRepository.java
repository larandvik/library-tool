package com.lav.libtool.repository;

import com.lav.libtool.entity.BookLoan;
import com.lav.libtool.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookLoanRepository extends JpaRepository<BookLoan, Long> {

    List<BookLoan> findByReaderId(Long readerId);

    List<BookLoan> findByStatusAndDueDateBefore(Status status, LocalDate date);

}
