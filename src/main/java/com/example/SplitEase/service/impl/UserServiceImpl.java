package com.example.SplitEase.service.impl;

import com.example.SplitEase.dto.request.LoginRequest;
import com.example.SplitEase.dto.request.RegisterRequest;
import com.example.SplitEase.dto.response.UserResponse;
import com.example.SplitEase.entity.UserEntity;
import com.example.SplitEase.exception.UserNotFoundException;
import com.example.SplitEase.repository.UserRepository;
import com.example.SplitEase.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse registerUser(RegisterRequest registerRequest) {
        UserEntity userEntity = new UserEntity();

        userEntity.setEmailId(registerRequest.getEmailId());
        userEntity.setFirstName(registerRequest.getFirstName());
        userEntity.setLastName(registerRequest.getLastName());
        userEntity.setRole("USER");

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        userEntity.setPassword(encodedPassword);

        UserEntity persistedUser = userRepository.save(userEntity);
        return UserResponse.convertToDTO(persistedUser);
    }

    @Cacheable(value = "userResponse", key = "#emailId")
    public UserResponse getUserByEmailId(String emailId) {
        UserEntity userEntity = userRepository.findByEmailId(emailId)
                .orElseThrow(() -> new UserNotFoundException("invalid emailId " + emailId));
        return UserResponse.convertToDTO(userEntity);
    }

    public void verifyCredentials(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username, request.password)
        );
    }
}
