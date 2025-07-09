package com.example.SplitEase.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ExpenseRequest {
    private Long userId;
    private Long groupId;
    private Double amount;
    private String description;
    private String splitOption;
    private List<SplitRequest> splits;
}
