package com.example.SplitEase.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "this field is required")
    private String firstName;

    @NotBlank(message = "this field is required")
    private String lastName;

    @NotBlank(message = "this field is required")
    @Email(message = "this is not a valid email address")
    private String emailId;

    @NotBlank(message = "this field is required")
    private String password;
}
