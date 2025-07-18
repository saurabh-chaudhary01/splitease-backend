package com.example.SplitEase.dto.response;

import com.example.SplitEase.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String emailId;
    private String role;

    public static UserResponse convertToDTO(UserEntity entity) {
        return UserResponse.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .emailId(entity.getEmailId())
                .role(entity.getRole())
                .build();
    }
}
