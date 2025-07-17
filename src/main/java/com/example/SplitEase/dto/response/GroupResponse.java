package com.example.SplitEase.dto.response;

import com.example.SplitEase.entity.GroupEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupResponse {
    private Long id;
    private String name;
    private UserResponse owner;
    private List<UserResponse> members;
    private List<ExpenseResponse> expenses;
    private LocalDateTime createdAt;

    public static GroupResponse convertToDTO(GroupEntity entity) {
        return GroupResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .owner(UserResponse.convertToDTO(entity.getOwner()))
                .members(entity.getMembers().stream()
                        .map(UserResponse::convertToDTO)
                        .toList()
                )
                .expenses(entity.getExpenses().stream()
                        .map(ExpenseResponse::convertToDTO)
                        .toList()
                )
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
