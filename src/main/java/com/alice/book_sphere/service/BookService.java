package com.alice.book_sphere.service;

import com.alice.book_sphere.database.entity.Book;
import com.alice.book_sphere.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Page<Book> findPage(int page, int size) {
        return bookRepository.findAll(PageRequest.of(page, size));
    }
}
