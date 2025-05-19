package com.alice.book_sphere.http.handler;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String email) {
        super("User Not Found with email: " + email);
    }
}
