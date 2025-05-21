package com.alice.book_sphere.service;

import com.alice.book_sphere.dto.BookDto;
import com.alice.book_sphere.dto.external.OpenLibrarySearchResponse;
import com.alice.book_sphere.dto.external.OpenLibraryWorkResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExternalBookService {
    private final WebClient externalBookWebClient;

    public List<BookDto> searchBooks(String query) {
        OpenLibrarySearchResponse resp = externalBookWebClient.get()
                .uri(uri -> uri.path("/search.json").queryParam("q", query).build())
                .retrieve()
                .bodyToMono(OpenLibrarySearchResponse.class)
                .block();

        return resp.getDocs().stream()
                .map(doc -> {
                    String key = doc.getKey();
                    String rawDesc = fetchDescription(key);
                    String coverUrl = doc.getCoverId() != null
                            ? "https://covers.openlibrary.org/b/id/" + doc.getCoverId() + "-L.jpg"
                            : "/images/default-cover.png";
                    String contentUrl = key != null
                            ? "https://openlibrary.org" + key
                            : "";
                    String author = (doc.getAuthorName()!=null && !doc.getAuthorName().isEmpty())
                            ? doc.getAuthorName().getFirst()
                            : "Unknown";

                    return new BookDto(
                            doc.getTitle(),
                            author,
                            coverUrl,
                            rawDesc,
                            contentUrl
                    );
                })
                .toList();
    }

    private String fetchDescription(String workKey) {
        if (workKey == null) return "";
        try {
            OpenLibraryWorkResponse work = externalBookWebClient.get()
                    .uri(uri -> uri.path(workKey + ".json").build())
                    .retrieve()
                    .bodyToMono(OpenLibraryWorkResponse.class)
                    .block();

            if (work == null || work.getDescription() == null) {
                return "";
            }
            Object desc = work.getDescription();
            if (desc instanceof String) {
                return (String) desc;
            } else if (desc instanceof Map<?,?>) {
                Object v = ((Map<?,?>) desc).get("value");
                return v != null ? v.toString() : "";
            }
        } catch (Exception ignored) {

        }
        return "";
    }
}