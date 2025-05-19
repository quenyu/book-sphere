package com.alice.book_sphere.repository;

import com.alice.book_sphere.database.entity.FavoriteBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteBookRepository extends JpaRepository<FavoriteBook, Long> {
    int countByUserId(Long userId);
}
