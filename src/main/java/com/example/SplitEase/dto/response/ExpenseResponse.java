package com.example.SplitEase.dto.response;

import com.example.SplitEase.entity.ExpenseEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ExpenseResponse {
    private Long id;
    private Double amount;
    private String description;
    private String splitOption;
    private UserResponse paidBy;
    private List<SplitResponse> splits;

    public static ExpenseResponse convertToDTO(ExpenseEntity entity) {
        return ExpenseResponse.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .description(entity.getDescription())
                .splitOption(entity.getSplitOption())
                .paidBy(UserResponse.convertToDTO(entity.getPaidBy()))
                .splits(entity.getSplits()
                        .stream()
                        .map(SplitResponse::convertToDTO)
                        .toList()
                )
                .build();
    }
}
