package com.serethewind.NeoClantech.service.service.serviceImpl;

import com.serethewind.NeoClantech.dto.TransactionDto;
import com.serethewind.NeoClantech.entity.Transaction;
import com.serethewind.NeoClantech.repository.TransactionRepository;
import com.serethewind.NeoClantech.service.TransactionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        Transaction newTransaction = Transaction.builder()
                .transactionType(transactionDto.getTransactionType())
                .accountNumber(transactionDto.getAccountNumber())
                .amount(transactionDto.getAmount())
                .build();

        transactionRepository.save(newTransaction);
    }

    @Override
    public List<TransactionDto> fetchTransactionByUser(String accountNumber) {
        List<Transaction> transactionList = transactionRepository.findByAccountNumber(accountNumber);

        List<TransactionDto> transactionDtoList = transactionList.stream().map(item -> TransactionDto.builder()
                .transactionType(item.getTransactionType())
                .accountNumber(item.getAccountNumber())
                .amount(item.getAmount())
                .build()).collect(Collectors.toList());

        return transactionDtoList;
    }
}
