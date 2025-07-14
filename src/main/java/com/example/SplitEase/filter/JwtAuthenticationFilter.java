package com.example.SplitEase.filter;

import com.example.SplitEase.security.CustomAuthEntryPoint;
import com.example.SplitEase.security.JwtAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AuthenticationManager authenticationManager;
    private final CustomAuthEntryPoint authEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractJwtToken(request);

        if (token != null) {
            try {
                JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(token);
                Authentication authResult = authenticationManager.authenticate(authenticationToken);
                if (authResult.isAuthenticated()) {
                    SecurityContextHolder.getContext().setAuthentication(authResult);
                }
            } catch (AuthenticationException e) {
                SecurityContextHolder.clearContext();
                authEntryPoint.commence(request, response, e);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractJwtToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
