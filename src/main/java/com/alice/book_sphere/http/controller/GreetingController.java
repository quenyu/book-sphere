package com.alice.book_sphere.http.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GreetingController {

    @GetMapping("/")
    public String home() {
        return "index";
    }
}
