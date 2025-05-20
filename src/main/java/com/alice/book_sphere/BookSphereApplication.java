package com.alice.book_sphere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BookSphereApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookSphereApplication.class, args);
	}

}
