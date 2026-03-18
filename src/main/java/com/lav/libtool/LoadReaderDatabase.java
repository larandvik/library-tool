package com.lav.libtool;

import com.lav.libtool.entity.Reader;
import com.lav.libtool.repository.ReaderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

@Slf4j
@Configuration
@Profile("dev")
@Order(20)
public class LoadReaderDatabase {

    @Bean
    CommandLineRunner initReaderDatabase(ReaderRepository repository) {
        return args -> {
            log.info("Preloading {}", repository.save(new Reader(
                    "Andrei",
                    "Larionov",
                    "0658203@gmail.com",
                    "+7-925-06-58-203"

            )));
            log.info("Preloading {}", repository.save(new Reader(
                    "Ivan",
                    "Ivanov",
                    "testIvan@gmail.com",
                    "+7-111-111-11-11"

            )));
            log.info("Preloading {}", repository.save(new Reader(
                    "Petr",
                    "Petrov",
                    "testPetr@gmail.com",
                    "+7-222-222-22-22"

            )));
            log.info("Preloading {}", repository.save(new Reader(
                    "Olga",
                    "Petrova",
                    "testOlga@gmail.com",
                    "+7-333-333-33-33"

            )));

            log.info("Database preloaded with sample readers");
        };
    }
}
