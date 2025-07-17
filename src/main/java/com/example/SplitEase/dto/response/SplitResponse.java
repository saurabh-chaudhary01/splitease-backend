package com.example.SplitEase.dto.response;

import com.example.SplitEase.entity.SplitEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SplitResponse {
    private Long id;
    private Double amount;
    private Integer share;
    private Integer percentage;
    private UserResponse owedBy;

    public static SplitResponse convertToDTO(SplitEntity entity) {
        return SplitResponse.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .share(entity.getShare())
                .percentage(entity.getPercentage())
                .owedBy(UserResponse.convertToDTO(entity.getOwedBy()))
                .build();
    }
}
