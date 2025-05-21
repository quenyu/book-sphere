package com.alice.book_sphere.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OpenLibraryWorkResponse {

    @JsonProperty("description")
    private Object description;
}