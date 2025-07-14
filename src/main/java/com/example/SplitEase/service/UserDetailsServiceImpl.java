package com.example.SplitEase.service;

import com.example.SplitEase.entity.UserEntity;
import com.example.SplitEase.entity.UserPrincipal;
import com.example.SplitEase.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
        UserEntity entity = userRepository
                .findByEmailId(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserPrincipal principal = new UserPrincipal();
        
        principal.setId(entity.getId());
        principal.setEmail(entity.getEmailId());
        principal.setPassword(entity.getPassword());
        principal.setRole(entity.getRole());

        return principal;
    }
}
