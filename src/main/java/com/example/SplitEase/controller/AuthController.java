package com.example.SplitEase.controller;

import com.example.SplitEase.dto.request.LoginRequest;
import com.example.SplitEase.dto.request.RegisterRequest;
import com.example.SplitEase.dto.response.TokenResponse;
import com.example.SplitEase.dto.response.UserResponse;
import com.example.SplitEase.service.UserService;
import com.example.SplitEase.utility.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication APIs", description = "Endpoints for user registration, login, refresh, and logout.")
public class AuthController {
    private static final int ACCESS_TOKEN_EXPIRY_MINUTES = 15;
    private static final int REFRESH_TOKEN_EXPIRY_DAYS = 7;

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse registerUser(@Valid @RequestBody RegisterRequest dto) {
        return userService.registerUser(dto);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user and generate access/refresh tokens")
    public ResponseEntity<TokenResponse> generateToken(HttpServletResponse response,
                                                       @Valid @RequestBody LoginRequest loginRequest) {
        userService.verifyCredentials(loginRequest);

        String accessToken = jwtTokenUtil.generateToken(loginRequest.getUsername(), ACCESS_TOKEN_EXPIRY_MINUTES);
        String refreshToken = jwtTokenUtil.generateToken(loginRequest.getUsername(),
                Duration.ofDays(REFRESH_TOKEN_EXPIRY_DAYS).toMinutes());

        Cookie refreshCookie = createRefreshTokenCookie(refreshToken);
        response.addCookie(refreshCookie);

        return ResponseEntity.ok(new TokenResponse("token generated successfully", accessToken));
    }

    @PostMapping("/refresh")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Refresh access token using refresh token cookie",
            description = "Requires a valid HttpOnly refresh token cookie to be present in the request."
    )
    public ResponseEntity<TokenResponse> refreshToken(Principal principal) {
        String username = principal.getName();
        String newAccessToken = jwtTokenUtil.generateToken(username, ACCESS_TOKEN_EXPIRY_MINUTES);
        return ResponseEntity.ok(new TokenResponse("token generated successfully", newAccessToken));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout the current user and clear refresh token")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie refreshCookie = new Cookie("refreshToken", "");
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/api/auth/refresh");
        refreshCookie.setMaxAge(0);

        response.addCookie(refreshCookie);
        return ResponseEntity.ok().build();
    }

    private Cookie createRefreshTokenCookie(String token) {
        Cookie cookie = new Cookie("refreshToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/api/auth/refresh");
        cookie.setMaxAge((int) Duration.ofDays(REFRESH_TOKEN_EXPIRY_DAYS).toSeconds());
        return cookie;
    }
}
