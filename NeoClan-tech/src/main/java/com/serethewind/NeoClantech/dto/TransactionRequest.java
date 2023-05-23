package com.serethewind.NeoClantech.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
    private String accountNumber;
    private BigDecimal amount;
}
