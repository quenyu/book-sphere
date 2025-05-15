package com.alice.book_sphere;

import org.springframework.boot.SpringApplication;

public class TestBookSphereApplication {

	public static void main(String[] args) {
		SpringApplication.from(BookSphereApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
