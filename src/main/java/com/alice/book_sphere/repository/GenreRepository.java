package com.alice.book_sphere.repository;

import com.alice.book_sphere.database.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.redis.core.RedisHash;

import java.util.Optional;

@RedisHash
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByNameIgnoreCase(String name);
}
