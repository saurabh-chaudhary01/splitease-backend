package com.example.SplitEase.service;

import com.example.SplitEase.dto.request.LoginRequest;
import com.example.SplitEase.dto.request.RegisterRequest;
import com.example.SplitEase.dto.response.UserResponse;

public interface UserService {
    UserResponse registerUser(RegisterRequest registerRequest);

    UserResponse getUserByEmailId(String emailId);

    void verifyCredentials(LoginRequest request);
}
