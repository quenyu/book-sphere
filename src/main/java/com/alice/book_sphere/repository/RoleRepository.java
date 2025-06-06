package com.alice.book_sphere.repository;

import com.alice.book_sphere.database.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.redis.core.RedisHash;

import java.util.Optional;

@RedisHash
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);

}
