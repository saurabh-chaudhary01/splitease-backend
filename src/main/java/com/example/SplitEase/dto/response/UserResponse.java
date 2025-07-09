package com.example.SplitEase.dto.response;

import com.example.SplitEase.entity.UserEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String emailId;

    public static UserResponse convertToDTO(UserEntity entity) {
        return UserResponse.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .emailId(entity.getEmailId())
                .build();
    }
}
