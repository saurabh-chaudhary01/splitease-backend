package com.example.SplitEase.dto.response;

import com.example.SplitEase.entity.ExpenseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseResponse {
    private Long id;
    private Double amount;
    private String description;
    private String splitOption;
    private UserResponse payer;
    private List<SplitResponse> splits;

    public static ExpenseResponse convertToDTO(ExpenseEntity entity) {
        return ExpenseResponse.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .description(entity.getDescription())
                .splitOption(entity.getSplitOption())
                .payer(UserResponse.convertToDTO(entity.getPayer()))
                .splits(entity.getSplits()
                        .stream()
                        .map(SplitResponse::convertToDTO)
                        .toList()
                )
                .build();
    }
}
