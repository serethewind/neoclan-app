package com.serethewind.NeoClantech.controller;

import com.serethewind.NeoClantech.dto.TransactionDto;
import com.serethewind.NeoClantech.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    private TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<List<TransactionDto>> fetchAllTransactionsByUser(@PathVariable String accountNumber){
        return new ResponseEntity<>(transactionService.fetchTransactionByUser(accountNumber), HttpStatus.OK);
    }
}
