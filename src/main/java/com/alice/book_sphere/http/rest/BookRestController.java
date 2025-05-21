package com.alice.book_sphere.http.rest;

import com.alice.book_sphere.database.entity.Book;
import com.alice.book_sphere.dto.BookDto;
import com.alice.book_sphere.service.BookService;
import com.alice.book_sphere.service.ExternalBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookRestController {
    private final ExternalBookService externalBookService;
    private final BookService bookService;

    @GetMapping("/search")
    public List<BookDto> search(@RequestParam("q") String query) {
        return externalBookService.searchBooks(query);
    }

    @GetMapping
    public Page<Book> page(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return bookService.findPage(page, size);
    }
}
