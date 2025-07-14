package com.example.SplitEase.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "this field is required")
    public String username;

    @NotBlank(message = "this field is required")
    public String password;
}
