package com.example.SplitEase.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExpenseResult {
    UserResponse payer;
    UserResponse payee;
    Double amount;
}
