package com.lav.libtool;

import com.lav.libtool.entity.BookLoan;
import com.lav.libtool.repository.BookLoanRepository;
import com.lav.libtool.repository.BookRepository;
import com.lav.libtool.repository.ReaderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import java.time.LocalDate;

@Configuration
@Slf4j
@Profile("dev")
@Order(30)
public class LoadBookLoanDatabase {

    @Bean
    CommandLineRunner initBookLoanDatabase(BookLoanRepository loanRepo,
                                           BookRepository bookRepo,
                                           ReaderRepository readerRepo) {
        return  args -> {
            log.info("Preloading {}", loanRepo.save(new BookLoan(
                    bookRepo.getReferenceById(1L),
                    readerRepo.getReferenceById(1L),
                    LocalDate.now().plusDays(1)
            )));
            log.info("Preloading {}", loanRepo.save(new BookLoan(
                    bookRepo.getReferenceById(2L),
                    readerRepo.getReferenceById(2L),
                    LocalDate.now().plusWeeks(1)
            )));
            log.info("Preloading {}", loanRepo.save(new BookLoan(
                    bookRepo.getReferenceById(3L),
                    readerRepo.getReferenceById(3L),
                    LocalDate.now().plusMonths(1)
            )));
            log.info("Preloading {}", loanRepo.save(new BookLoan(
                    bookRepo.getReferenceById(4L),
                    readerRepo.getReferenceById(4L),
                    LocalDate.now().plusYears(1)
            )));

            log.info("Database preloaded with sample readers");
        };
    }
}
