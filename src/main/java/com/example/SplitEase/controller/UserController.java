package com.example.SplitEase.controller;

import com.example.SplitEase.dto.response.UserResponse;
import com.example.SplitEase.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public UserResponse userProfile(Principal principal) {
        String email = principal.getName();
        return userService.getUserByEmailId(email);
    }
}
