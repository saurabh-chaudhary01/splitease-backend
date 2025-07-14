package com.example.SplitEase.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class GroupRequest {
    @NotBlank(message = "this field is required")
    private String name;

    @NotNull(message = "this field is required")
    private Long ownerUserId;

    @NotNull(message = "this field is required")
    private List<Long> members;
}
