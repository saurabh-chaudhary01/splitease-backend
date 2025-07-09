package com.example.SplitEase.service;

import com.example.SplitEase.dto.request.LoginRequest;
import com.example.SplitEase.dto.request.RegisterRequest;
import com.example.SplitEase.dto.response.UserResponse;
import com.example.SplitEase.entity.UserEntity;
import com.example.SplitEase.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public UserResponse registerUser(RegisterRequest registerRequest) {
        UserEntity userEntity = new UserEntity();

        userEntity.setEmailId(registerRequest.getEmailId());
        userEntity.setFirstName(registerRequest.getFirstName());
        userEntity.setLastName(registerRequest.getLastName());

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        userEntity.setPassword(encodedPassword);

        UserEntity persistedUser = userRepository.save(userEntity);

        return UserResponse.builder()
                .id(persistedUser.getId())
                .firstName(persistedUser.getFirstName())
                .lastName(persistedUser.getLastName())
                .emailId(persistedUser.getEmailId())
                .build();
    }

    public UserResponse getUserByEmailId(String emailId) {
        UserEntity userEntity = userRepository.findByEmailId(emailId).orElseThrow();
        return UserResponse.builder()
                .id(userEntity.getId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .emailId(userEntity.getEmailId())
                .build();
    }

    public void verifyCredential(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username, request.password)
        );
    }
}
