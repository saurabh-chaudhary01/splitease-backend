package com.example.SplitEase.security;

import com.example.SplitEase.utility.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof JwtAuthenticationToken jwtAuthToken)) {
            throw new BadCredentialsException("Unsupported authentication type");
        }

        String token = jwtAuthToken.getToken();

        try {
            String username = jwtTokenUtil.validateAndExtractUsername(token);
            if (username == null || username.isBlank()) {
                throw new BadCredentialsException("MISSING_SUBJECT");
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        } catch (ExpiredJwtException ex) {
            throw new BadCredentialsException("TOKEN_EXPIRED");
        } catch (MalformedJwtException | SignatureException | IllegalArgumentException ex) {
            throw new BadCredentialsException("INVALID_TOKEN");
        } catch (UsernameNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AuthenticationServiceException("Authentication failed: " + ex.getMessage());
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
