package com.serethewind.NeoClantech.service;

import com.serethewind.NeoClantech.dto.Response;
import com.serethewind.NeoClantech.dto.TransactionRequest;
import com.serethewind.NeoClantech.dto.TransferRequest;
import com.serethewind.NeoClantech.dto.UserRequest;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {

    Response registerUser(UserRequest userRequest);

    List<Response> fetchAll();

    Response fetchSingleUserById(Long userId)   ;

    Response updateUserById(Long id, UserRequest userRequest);

    Response balanceEnquiry(String accountNumber);

    Response nameEnquiry(String accountNumber);

    Response creditRequest(TransactionRequest transactionRequest);

    Response debitRequest(TransactionRequest transactionRequest);

    Response transferRequest(TransferRequest transferRequest);
}
