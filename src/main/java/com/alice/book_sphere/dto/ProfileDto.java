package com.alice.book_sphere.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileDto {
    private int favoriteBooks;
    private int booksRead;
    private int activeDays;
}