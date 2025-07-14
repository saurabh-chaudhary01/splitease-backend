package com.example.SplitEase.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class ExpenseRequest {
    @NotNull(message = "this field is required")
    private Long userId;

    @NotNull(message = "this field is required")
    private Long groupId;

    @NotNull(message = "this field is required")
    @Min(value = 0, message = "amount must be positive")
    private Double amount;

    @NotBlank(message = "this field is required")
    @Size(min = 5, max = 200)
    private String description;

    @NotNull(message = "this is field is required")
    @Pattern(regexp = "^(equally|share|percentage)$", message = "value must be one of: equally, share, percentage")
    private String splitOption;

    @NotNull(message = "this field is required")
    private List<SplitRequest> splits;
}
