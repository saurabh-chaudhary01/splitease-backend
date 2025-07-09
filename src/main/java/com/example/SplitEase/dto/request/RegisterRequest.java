package com.example.SplitEase.dto.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String emailId;
    private String password;
}
