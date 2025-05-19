package com.alice.book_sphere.http.controller;

import com.alice.book_sphere.database.entity.User;
import com.alice.book_sphere.service.StatsService;
import com.alice.book_sphere.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ProfileController {
    private final UserService userService;
    private final StatsService statsService;

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("stats", statsService.getUserStats(user.getId()));
        model.addAttribute("recentActivities", statsService.getRecentActivities(user.getId()));
        return "profile";
    }
}
