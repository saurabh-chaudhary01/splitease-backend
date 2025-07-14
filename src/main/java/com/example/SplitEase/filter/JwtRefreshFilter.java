package com.example.SplitEase.filter;

import com.example.SplitEase.security.CustomAuthEntryPoint;
import com.example.SplitEase.security.JwtAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtRefreshFilter extends OncePerRequestFilter {
    private final AuthenticationManager authenticationManager;
    private final CustomAuthEntryPoint authEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!request.getServletPath().equals("/api/auth/refresh")) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = extractJwtFromRequest(request);

        if (refreshToken == null) {
            return;
        }

        try {
            JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(refreshToken);
            Authentication authResult = authenticationManager.authenticate(authenticationToken);

            if (authResult.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authResult);
            }
        } catch (AuthenticationException e) {
            SecurityContextHolder.clearContext();
            authEntryPoint.commence(request, response, e);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
