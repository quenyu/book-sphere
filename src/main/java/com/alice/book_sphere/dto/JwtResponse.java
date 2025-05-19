package com.alice.book_sphere.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String type = "Bearer";
    private String accessToken;
    private String email;
    private List<String> roles;
    private long expiresIn;

    public JwtResponse(String accessToken, String email, List<String> roles, long expiresIn) {
        this.accessToken = accessToken;
        this.email = email;
        this.roles = roles;
        this.expiresIn = expiresIn;
    }
}