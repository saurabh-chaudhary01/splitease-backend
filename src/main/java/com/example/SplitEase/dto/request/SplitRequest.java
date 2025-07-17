package com.example.SplitEase.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SplitRequest {
    @NotNull(message = "this filed is required")
    private Long paidByUserId;

    @NotNull(message = "this filed is required")
    @Min(value = 0, message = "amount must be positive")
    private Double amount;

    @NotNull(message = "this filed is required")
    private Integer share;

    @NotNull(message = "this filed is required")
    @Min(value = 0, message = "invalid percentage value")
    @Max(value = 100, message = "invalid percentage value")
    private Integer percentage;
}
