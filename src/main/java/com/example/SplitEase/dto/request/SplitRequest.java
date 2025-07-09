package com.example.SplitEase.dto.request;

import lombok.Data;

@Data
public class SplitRequest {
    private Long userId;
    private Double amount;
    private Integer share;
    private Integer percentage;
}
