package com.alice.book_sphere.repository;

import com.alice.book_sphere.database.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
