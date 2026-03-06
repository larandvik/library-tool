package com.lav.libtool;

import com.lav.libtool.entity.Book;
import com.lav.libtool.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoadBookDatabase {

    @Bean
    CommandLineRunner initBookDatabase(BookRepository repository) {
        return args -> {
            log.info("Preloading {}", repository.save(new Book(
                    "Clean Code",
                    "Robert C. Martin",
                    "9780132350884",
                    2008,
                    5
            )));
            log.info("Preloading {}", repository.save(new Book(
                    "Effective Java",
                    "Joshua Bloch",
                    "9780134685991",
                    2018,
                    3
            )));
            log.info("Preloading {}", repository.save(repository.save(new Book(
                    "Spring in Action",
                    "Craig Walls",
                    "9781617294945",
                    2018,
                    4
            ))));
            log.info("Preload {}", repository.save(new Book(
                    "Design Patterns",
                    "Erich Gamma",
                    "9780201633610",
                    1994,
                    2
            )));

            log.info("Database preloaded with sample books");
        };
    }

}
