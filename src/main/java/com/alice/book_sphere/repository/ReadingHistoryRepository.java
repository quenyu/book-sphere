package com.alice.book_sphere.repository;

import com.alice.book_sphere.database.entity.ReadingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.repository.query.Param;

import java.util.List;

@RedisHash
public interface ReadingHistoryRepository extends JpaRepository<ReadingHistory, Long> {
    @Query("SELECT COUNT(DISTINCT DATE(rh.startDate)) FROM ReadingHistory rh WHERE rh.user.id = :userId")
    int countActiveDays(@Param("userId") Long userId);

    @Query("SELECT COUNT(rh) FROM ReadingHistory rh WHERE rh.user.id = :userId AND rh.progress = 100.0")
    int countCompletedBooks(@Param("userId") Long userId);

    List<ReadingHistory> findTop5ByUserIdOrderByLastOpenedDateDesc(Long userId);
}
