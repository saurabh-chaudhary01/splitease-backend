package com.example.SplitEase.service.impl;

import com.example.SplitEase.entity.UserEntity;
import com.example.SplitEase.entity.UserPrincipal;
import com.example.SplitEase.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = getUserByEmailId(username);

        UserPrincipal principal = new UserPrincipal();
        principal.setId(user.getId());
        principal.setUsername(user.getEmailId());
        principal.setPassword(user.getPassword());
        principal.setRole(user.getRole());

        return principal;
    }

    @Cacheable(value = "userEntity", key = "#emailId")
    private UserEntity getUserByEmailId(String emailId) {
        return userRepository
                .findByEmailId(emailId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
