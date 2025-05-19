package com.alice.book_sphere.service;

import com.alice.book_sphere.database.entity.ReadingHistory;
import com.alice.book_sphere.dto.ProfileDto;
import com.alice.book_sphere.dto.RecentActivityDto;
import com.alice.book_sphere.repository.FavoriteBookRepository;
import com.alice.book_sphere.repository.ReadingHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final FavoriteBookRepository favoriteBookRepository;
    private final ReadingHistoryRepository readingHistoryRepository;

    public ProfileDto getUserStats(Long userId) {
        return new ProfileDto(
                favoriteBookRepository.countByUserId(userId),
                readingHistoryRepository.countCompletedBooks(userId),
                readingHistoryRepository.countActiveDays(userId)
        );
    }

    public List<RecentActivityDto> getRecentActivities(Long userId) {
        return readingHistoryRepository.findTop5ByUserIdOrderByLastOpenedDateDesc(userId)
                .stream()
                .map(this::mapToActivity)
                .toList();
    }

    private RecentActivityDto mapToActivity(ReadingHistory history) {
        return new RecentActivityDto(
                history.getBook().getTitle(),
                history.getProgress() + "%",
                history.getLastOpenedDate(),
                "Чтение"
        );
    }
}
