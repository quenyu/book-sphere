package com.alice.book_sphere.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OpenLibrarySearchResponse {
    private List<Doc> docs;

    @Data
    public static class Doc {
        @JsonProperty("title")
        private String title;

        @JsonProperty("author_name")
        private List<String> authorName;

        @JsonProperty("cover_i")
        private Integer coverId;

        @JsonProperty("key")
        private String key;
    }
}