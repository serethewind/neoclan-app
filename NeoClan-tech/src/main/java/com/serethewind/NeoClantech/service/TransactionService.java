package com.serethewind.NeoClantech.service;

import com.serethewind.NeoClantech.dto.TransactionDto;

import java.util.List;

public interface TransactionService {

    void saveTransaction(TransactionDto transactionDto);

    List<TransactionDto> fetchTransactionByUser(String accountNumber);

    List<TransactionDto> fetchCreditOrDebitTransactionByUser(String accountNumber, String debitOrCredit);
}
