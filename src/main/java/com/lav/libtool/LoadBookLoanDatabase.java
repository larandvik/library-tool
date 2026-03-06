package com.lav.libtool;

import com.lav.libtool.entity.BookLoan;
import com.lav.libtool.repository.BookLoanRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
@Slf4j
public class LoadBookLoanDatabase {

    @Bean
    CommandLineRunner initBookLoanDatabase(BookLoanRepository repository) {
        return  args -> {
            log.info("Preloading {}", repository.save(new BookLoan(
                    1L,
                    1L,
                    LocalDate.now().plusDays(1)
            )));
            log.info("Preloading {}", repository.save(new BookLoan(
                    2L,
                    2L,
                    LocalDate.now().plusWeeks(1)
            )));
            log.info("Preloading {}", repository.save(new BookLoan(
                    3L,
                    3L,
                    LocalDate.now().plusMonths(1)
            )));
            log.info("Preloading {}", repository.save(new BookLoan(
                    4L,
                    4L,
                    LocalDate.now().plusYears(1)
            )));

            log.info("Database preloaded with sample readers");
        };
    }
}
