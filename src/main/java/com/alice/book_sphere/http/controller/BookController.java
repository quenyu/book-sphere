package com.alice.book_sphere.http.controller;

import com.alice.book_sphere.database.entity.Book;
import com.alice.book_sphere.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/books")
    public String books(Model model) {
        int initialPage = 0, size = 20;
        Page<Book> page = bookService.findPage(initialPage, size);
        model.addAttribute("booksPage", page);
        model.addAttribute("size", size);
        return "books";
    }
}
