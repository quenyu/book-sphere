package com.alice.book_sphere.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RecentActivityDto {
    private String bookTitle;
    private String progress;
    private LocalDateTime date;
    private String type;
}
