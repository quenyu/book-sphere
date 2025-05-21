package com.alice.book_sphere.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookDto {
    private String title;
    private String author;
    private String coverUrl;
    private String description;
    private String contentUrl;
}

