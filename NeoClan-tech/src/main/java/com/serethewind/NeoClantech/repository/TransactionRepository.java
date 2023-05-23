package com.serethewind.NeoClantech.repository;

import com.serethewind.NeoClantech.dto.TransactionDto;
import com.serethewind.NeoClantech.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TransactionRepository extends JpaRepository<Transaction, String> {

    List<Transaction> findByAccountNumber(String accountNumber);
}
