package com.example.SplitEase.dto.response;

import com.example.SplitEase.entity.SplitEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SplitResponse {
    private Long id;
    private Double amount;
    private Integer share;
    private Integer percentage;
    private UserResponse user;

    public static SplitResponse convertToDTO(SplitEntity entity) {
        return SplitResponse.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .share(entity.getShare())
                .percentage(entity.getPercentage())
                .user(UserResponse.convertToDTO(entity.getUser()))
                .build();
    }
}
