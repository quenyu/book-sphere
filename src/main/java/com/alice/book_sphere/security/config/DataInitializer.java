package com.alice.book_sphere.security.config;

import com.alice.book_sphere.database.entity.Book;
import com.alice.book_sphere.database.entity.Genre;
import com.alice.book_sphere.dto.BookDto;
import com.alice.book_sphere.repository.BookRepository;
import com.alice.book_sphere.repository.GenreRepository;
import com.alice.book_sphere.service.ExternalBookService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final ExternalBookService externalBookService;
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;

    private final Faker faker = new Faker();
    private final AtomicInteger fakeCounter = new AtomicInteger();

    @PostConstruct
    @Transactional
    public void init() {
        if (bookRepository.count() > 0) {
            log.info("Books already initialized, skipping import.");
            return;
        }

        List<String> subjects = List.of("Fantasy", "Science Fiction", "Romance", "Mystery", "History");
        subjects.forEach(this::importBySubject);

        generateFakeBooks(200);
    }

    private void importBySubject(String subject) {
        log.info("Importing subject '{}'", subject);
        List<BookDto> dtoList;
        try {
            dtoList = externalBookService.searchBooks(subject);
        } catch (WebClientResponseException e) {
            log.warn("API unavailable for subject '{}': {}", subject, e.getStatusCode());
            return;
        }
        Genre genre = genreRepository.findByNameIgnoreCase(subject)
                .orElseGet(() -> genreRepository.save(new Genre(null, subject)));

        dtoList.stream().limit(50)
                .map(dto -> dtoToEntity(dto, genre))
                .forEach(bookRepository::save);

        log.info("Imported {} books for '{}'", Math.min(dtoList.size(), 50), subject);
    }


    private void generateFakeBooks(int targetTotal) {
        int existing = (int) bookRepository.count();
        int toGenerate = Math.max(0, targetTotal - existing);
        log.info("Generating {} fake books", toGenerate);

        Genre defaultGenre = genreRepository.findAll()
                .stream().findAny()
                .orElseGet(() -> genreRepository.save(new Genre(null, "General")));

        for (int i = 0; i < toGenerate; i++) {
            Book b = new Book();
            b.setTitle(faker.book().title() + " #" + fakeCounter.incrementAndGet());
            b.setAuthor(faker.book().author());
            b.setDescription(faker.lorem().sentence(10));
            b.setCoverUrl("https://picsum.photos/200/300?random=" + faker.random().nextInt(1, 1000));
            b.setContentUrl("https://example.com/content/" + UUID.randomUUID());
            b.getGenres().add(defaultGenre);
            bookRepository.save(b);
        }

        log.info("Fake books generated");
    }

    private Book dtoToEntity(BookDto dto, Genre genre) {
        Book b = new Book();
        b.setTitle(dto.getTitle());
        b.setAuthor(dto.getAuthor());
        b.setDescription(dto.getDescription());
        b.setCoverUrl(dto.getCoverUrl() != null ? dto.getCoverUrl() : "/images/default-cover.png");
        b.setContentUrl(dto.getContentUrl() != null ? dto.getContentUrl() : "");
        b.getGenres().add(genre);
        return b;
    }
}