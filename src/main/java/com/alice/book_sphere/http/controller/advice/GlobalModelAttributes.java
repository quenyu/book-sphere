package com.alice.book_sphere.http.controller.advice;

import com.alice.book_sphere.database.entity.User;
import com.alice.book_sphere.dto.ProfileDto;
import com.alice.book_sphere.service.StatsService;
import com.alice.book_sphere.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributes {
    private final StatsService statsService;
    private final UserService userService;

    @ModelAttribute
    public void addStatsToModel(Model model,
                                @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            User user = userService.findByEmail(userDetails.getUsername());
            ProfileDto stats = statsService.getUserStats(user.getId());
            model.addAttribute("stats", stats);
        }
    }
}
