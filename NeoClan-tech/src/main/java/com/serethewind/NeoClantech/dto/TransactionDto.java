package com.serethewind.NeoClantech.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private String transactionType;
    private String accountNumber;
    private BigDecimal amount;
}
