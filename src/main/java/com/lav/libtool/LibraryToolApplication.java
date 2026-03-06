package com.lav.libtool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class LibraryToolApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryToolApplication.class, args);
	}

}
