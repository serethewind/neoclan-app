package com.serethewind.NeoClantech.service.service.serviceImpl;

import com.serethewind.NeoClantech.dto.*;
import com.serethewind.NeoClantech.entity.User;
import com.serethewind.NeoClantech.repository.UserRepository;
import com.serethewind.NeoClantech.service.EmailService;
import com.serethewind.NeoClantech.service.SmsService;
import com.serethewind.NeoClantech.service.TransactionService;
import com.serethewind.NeoClantech.service.UserService;
import com.serethewind.NeoClantech.util.ResponseUtils;
import jakarta.validation.constraints.Email;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private TransactionService transactionService;
    private EmailService emailService;

    private SmsService smsService;

    private ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, TransactionService transactionService, ModelMapper modelMapper, EmailService emailService, SmsService smsService) {
        this.userRepository = userRepository;
        this.transactionService = transactionService;
        this.modelMapper = modelMapper;
        this.emailService = emailService;
        this.smsService = smsService;
    }

    @Override
    public Response registerUser(UserRequest userRequest) {
        /**
         *
         */
        boolean isEmailExist = userRepository.existsByEmail(userRequest.getEmail());
        if (isEmailExist) {
            return Response.builder().responseCode(ResponseUtils.USER_EXISTS_CODE).responseMessage(ResponseUtils.USER_EXISTS_MESSAGE).data(null).build();
        } else {
            User user = User.builder()
                    .firstName(userRequest.getFirstName())
                    .lastName(userRequest.getLastName())
                    .otherName(userRequest.getOtherName())
                    .gender(userRequest.getGender())
                    .address(userRequest.getAddress())
                    .stateOfOrigin(userRequest.getStateOfOrigin())
                    .accountNumber(ResponseUtils.generateAccountNumber(ResponseUtils.lengthOfAccountNumber))
                    .accountBalance(userRequest.getAccountBalance())
                    .email(userRequest.getEmail())
                    .phoneNumber(userRequest.getPhoneNumber())
                    .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                    .status("ACTIVE")
                    .dateOfBirth(userRequest.getDateOfBirth())
                    .build();

            User savedUser = userRepository.save(user);

            //send email

            String accountDetails = savedUser.getFirstName() + savedUser.getOtherName() + savedUser.getLastName() + "\nAccount " + savedUser.getAccountNumber();

            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(savedUser.getEmail())
                    .subject("ACCOUNT DETAILS")
                    .messageBody("Congratulations! Your account has been successfully created! Kindly find your details below: \n" + accountDetails)
                    .build();

            emailService.sendSimpleMessage(emailDetails);

            SMSDetails smsDetails = SMSDetails.builder()
                    .recipientNumber(savedUser.getPhoneNumber())
                    .textBody("Congratulations! Your account has been successfully created! Kindly find your details below: \n" + accountDetails)
                    .build();

            smsService.sendSMS(smsDetails);

            return Response.builder().responseCode(ResponseUtils.SUCCESS).responseMessage(ResponseUtils.USER_REGISTERED_SUCCESS).data(Data.builder()
                            .accountBalance(savedUser.getAccountBalance())
                            .accountNumber(savedUser.getAccountNumber())
                            .accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName())
                            .build())
                    .build();

        }
    }

    public List<Response> fetchAll() {
        List<User> userList = userRepository.findAll();
        List<Response> responseList = new ArrayList<>();
        for (User user : userList) {
            responseList.add(Response.builder()
                    .responseCode(ResponseUtils.USER_EXISTS_CODE)
                    .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                    .data(Data.builder()
                            .accountBalance(user.getAccountBalance())
                            .accountNumber(user.getAccountNumber())
                            .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                            .build()).
                    build());
        }
        return responseList;
    }

    @Override
    public Response fetchSingleUserById(Long id) {
//        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));


        if (!userRepository.existsById(id)) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }

        User user = userRepository.findById(id).get();
        return Response.builder()
                .responseCode(ResponseUtils.SUCCESS)
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .data(Data.builder()
                        .accountName(user.getFirstName() + " " + user.getOtherName() + " " + user.getLastName())
                        .accountNumber(user.getAccountNumber())
                        .accountBalance(user.getAccountBalance())
                        .build())
                .build();

    }

    @Override
    public Response updateUserById(Long id, UserRequest userRequest) {
        User updatedUser = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        modelMapper.map(userRequest, updatedUser);

//        User updatedUser = foundUser.builder()
//                .firstName(userRequest.getFirstName())
//                .lastName(userRequest.getLastName())
//                .otherName(userRequest.getOtherName())
//                .gender(userRequest.getGender())
//                .address(userRequest.getAddress())
//                .stateOfOrigin(userRequest.getStateOfOrigin())
//                .accountNumber(foundUser.getAccountNumber())
//                .accountBalance(userRequest.getAccountBalance())
//                .email(userRequest.getEmail())
//                .phoneNumber(userRequest.getPhoneNumber())
//                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
//                .status(foundUser.getStatus())
//                .dateOfBirth(userRequest.getDateOfBirth())
//                .build();


        userRepository.save(updatedUser);


        return Response.builder()
                .responseCode(ResponseUtils.SUCCESS)
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .data(Data.builder()
                        .accountName(updatedUser.getFirstName() + " " + updatedUser.getOtherName() + " " + updatedUser.getLastName())
                        .accountNumber(updatedUser.getAccountNumber())
                        .accountBalance(updatedUser.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public Response balanceEnquiry(String accountNumber) {
        boolean isAccountExist = userRepository.existsByAccountNumber(accountNumber);

        if (!isAccountExist) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null).
                    build();
        }

        User user = userRepository.findByAccountNumber(accountNumber);

        return Response.builder().
                responseCode(ResponseUtils.SUCCESS)
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .data(Data.builder()
                        .accountBalance(user.getAccountBalance())
                        .accountName(null)
                        .accountNumber(accountNumber)
                        .build()).
                build();
    }

    @Override
    public Response nameEnquiry(String accountNumber) {
        boolean isAccountExist = userRepository.existsByAccountNumber(accountNumber);

        if (!isAccountExist) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null).
                    build();
        }

        User user = userRepository.findByAccountNumber(accountNumber);

        return Response.builder().
                responseCode(ResponseUtils.SUCCESS)
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .data(Data.builder()
                        .accountBalance(null)
                        .accountName(user.getFirstName() + " " + user.getOtherName() + " " + user.getLastName())
                        .accountNumber(accountNumber)
                        .build()).
                build();
    }

    @Override
    @Transactional
    public Response creditRequest(TransactionRequest transactionRequest) {
        User receivingUser = userRepository.findByAccountNumber(transactionRequest.getAccountNumber());

        if (!userRepository.existsByAccountNumber(transactionRequest.getAccountNumber())) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }

        receivingUser.setAccountBalance(receivingUser.getAccountBalance().add(transactionRequest.getAmount()));
        TransactionDto transactionDto = TransactionDto.builder()
                .transactionType("CREDIT")
                .accountNumber(receivingUser.getAccountNumber())
                .amount(transactionRequest.getAmount())
                .build();

        userRepository.save(receivingUser);
        transactionService.saveTransaction(transactionDto);

        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient(receivingUser.getEmail());
        emailDetails.setSubject("NeoClan Tech Transaction Alert [Credit : " + transactionRequest.getAmount() + "]");
        emailDetails.setMessageBody("Credit transaction of " + transactionRequest.getAmount() + " has been performed on your account. Your new account balance is " + receivingUser.getAccountBalance());

        emailService.sendSimpleMessage(emailDetails);

        SMSDetails smsDetails = new SMSDetails();
        smsDetails.setRecipientNumber(receivingUser.getPhoneNumber());
        smsDetails.setTextBody("NeoClan Tech Transaction Alert [Credit : " + transactionRequest.getAmount() + "]\n Credit transaction of " + transactionRequest.getAmount() + " has been performed on your account. Your new account balance is " + receivingUser.getAccountBalance());

        smsService.sendSMS(smsDetails);

        return Response.builder()
                .responseCode(ResponseUtils.SUCCESSFUL_TRANSACTION)
                .responseMessage(ResponseUtils.ACCOUNT_CREDITED)
                .data(Data.builder()
                        .accountName(receivingUser.getFirstName() + " " + receivingUser.getOtherName() + " " + receivingUser.getLastName())
                        .accountBalance(receivingUser.getAccountBalance())
                        .accountNumber(receivingUser.getAccountNumber())
                        .build())
                .build();
    }


    @Override
    @Transactional
    public Response debitRequest(TransactionRequest transactionRequest) {
        User user = userRepository.findByAccountNumber(transactionRequest.getAccountNumber());

        if (!userRepository.existsByAccountNumber(transactionRequest.getAccountNumber())) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }

        if (transactionRequest.getAmount().compareTo(user.getAccountBalance()) == 1) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_BALANCE_ENQUIRY)
                    .responseMessage(ResponseUtils.ACCOUNT_BALANCE_INSUFFICIENT)
                    .data(null)
                    .build();
        }

        user.setAccountBalance(user.getAccountBalance().subtract(transactionRequest.getAmount()));

        TransactionDto transactionDto = TransactionDto.builder()
                .transactionType("DEBIT")
                .accountNumber(user.getAccountNumber())
                .amount(transactionRequest.getAmount())
                .build();

        transactionService.saveTransaction(transactionDto);
        userRepository.save(user);

        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient(user.getEmail());
        emailDetails.setSubject("NeoClan Tech Transaction Alert [Debit : " + transactionRequest.getAmount() + "]");
        emailDetails.setMessageBody("Debit transaction of " + transactionRequest.getAmount() + " has been performed on your account. Your new account balance is " + user.getAccountBalance());

        SMSDetails smsDetails = new SMSDetails();
        smsDetails.setRecipientNumber(user.getPhoneNumber());
        smsDetails.setTextBody("NeoClan Tech Transaction Alert [Debit : " + transactionRequest.getAmount() + "]\nDebit transaction of " + transactionRequest.getAmount() + " has been performed on your account. Your new account balance is " + user.getAccountBalance());

        smsService.sendSMS(smsDetails);

        emailService.sendSimpleMessage(emailDetails);

        return Response.builder()
                .responseCode(ResponseUtils.SUCCESSFUL_TRANSACTION)
                .responseMessage(ResponseUtils.ACCOUNT_DEBITED)
                .data(Data.builder()
                        .accountName(user.getFirstName() + " " + user.getOtherName() + " " + user.getLastName())
                        .accountBalance(user.getAccountBalance())
                        .accountNumber(user.getAccountNumber())
                        .build())
                .build();

    }

    //    @Override
//    public Response transferRequest(TransactionRequest transactionRequest, String receivingAccountNumber) {
//        User user = userRepository.findByAccountNumber(transactionRequest.getAccountNumber());
//        User receivingUser = userRepository.findByAccountNumber(receivingAccountNumber);
//
//        if (!userRepository.existsByAccountNumber(transactionRequest.getAccountNumber())) {
//            return Response.builder()
//                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
//                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
//                    .data(null)
//                    .build();
//        }
//
//        if (transactionRequest.getAmount().compareTo(user.getAccountBalance()) == 1) {
//            return Response.builder()
//                    .responseCode(ResponseUtils.USER_BALANCE_ENQUIRY)
//                    .responseMessage(ResponseUtils.ACCOUNT_BALANCE_INSUFFICIENT)
//                    .data(null)
//                    .build();
//        }
//
//        if (!userRepository.existsByAccountNumber(receivingAccountNumber)) {
//            return Response.builder()
//                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
//                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
//                    .data(null)
//                    .build();
//        }
//
//        //best condition
//        user.setAccountBalance(user.getAccountBalance().subtract(transactionRequest.getAmount()));
//        receivingUser.setAccountBalance(receivingUser.getAccountBalance().add(transactionRequest.getAmount()));
//        userRepository.save(user);
//        userRepository.save(receivingUser);
//
//        return Response.builder()
//                .responseCode(ResponseUtils.SUCCESSFUL_TRANSACTION)
//                .responseMessage(ResponseUtils.ACCOUNT_DEBITED_FOR_TRANSFER)
//                .data(Data.builder()
//                        .accountName(user.getFirstName() + " " + user.getOtherName() + " " + user.getLastName())
//                        .accountBalance(user.getAccountBalance())
//                        .accountNumber(user.getAccountNumber())
//                        .build())
//                .build();
//    }
    @Transactional
    public Response transferRequest(TransferRequest transferRequest) {
        User sendingUser = userRepository.findByAccountNumber(transferRequest.getSourceAccountNumber());
        User receivingUser = userRepository.findByAccountNumber(transferRequest.getDestinationAccountNumber());

        if (!userRepository.existsByAccountNumber(transferRequest.getDestinationAccountNumber())) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }

        //i want to pass the methods for debit and credit.
        //create instance of transactionRequest

        TransactionRequest sendingTransactionRequest = new TransactionRequest(sendingUser.getAccountNumber(), transferRequest.getAmount());
        TransactionRequest receivingTransactionRequest = new TransactionRequest(receivingUser.getAccountNumber(), transferRequest.getAmount());

        Response debitResponse = debitRequest(sendingTransactionRequest);

        if (!debitResponse.getResponseCode().equalsIgnoreCase(ResponseUtils.SUCCESSFUL_TRANSACTION)) {
            return debitResponse;
        }
        creditRequest(receivingTransactionRequest);

//
//        if (!userRepository.existsByAccountNumber(transferRequest.getSourceAccountNumber()) || !userRepository.existsByAccountNumber(transferRequest.getDestinationAccountNumber())) {
//            return Response.builder()
//                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
//                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
//                    .data(null)
//                    .build();
//        }
//
//        if (sendingUser.getAccountBalance().compareTo(transferRequest.getAmount()) == -1) {
//            return Response.builder()
//                    .responseCode(ResponseUtils.USER_BALANCE_ENQUIRY)
//                    .responseMessage(ResponseUtils.ACCOUNT_BALANCE_INSUFFICIENT)
//                    .data(null)
//                    .build();
//        }
//
//        sendingUser.setAccountBalance(sendingUser.getAccountBalance().subtract(transferRequest.getAmount()));
//        receivingUser.setAccountBalance(receivingUser.getAccountBalance().add(transferRequest.getAmount()));
//        userRepository.save(sendingUser);
//        userRepository.save(receivingUser);
//
//        TransactionDto debitTransactiondto = TransactionDto.builder()
//                .transactionType("DEBIT")
//                .accountNumber(sendingUser.getAccountNumber())
//                .amount(transferRequest.getAmount())
//                .build();
//
//        TransactionDto creditTransactiondto = TransactionDto.builder()
//                .transactionType("CREDIT")
//                .accountNumber(receivingUser.getAccountNumber())
//                .amount(transferRequest.getAmount())
//                .build();
//
//        EmailDetails emailDetails = new EmailDetails();
//
//        emailDetails.setRecipient(sendingUser.getEmail());
//        emailDetails.setSubject("NeoClan Tech Transaction Alert [Debit : " + transferRequest.getAmount() + "]");
//        emailDetails.setMessageBody("Transfer of " + transferRequest.getAmount() + " to " + receivingUser.getFirstName() + " " + receivingUser.getOtherName() + " " + receivingUser.getLastName() + " is successful.  Your new account balance is " + sendingUser.getAccountBalance());
//
//        emailService.sendSimpleMessage(emailDetails);
//
//        SMSDetails smsDetails = SMSDetails.builder()
//                .recipientNumber(sendingUser.getPhoneNumber())
//                .textBody("NeoClan Tech Transaction Alert [Debit : " + transferRequest.getAmount() + "]\nTransfer of " + transferRequest.getAmount() + " to " + receivingUser.getFirstName() + " " + receivingUser.getOtherName() + " " + receivingUser.getLastName() + " is successful.  Your new account balance is " + sendingUser.getAccountBalance())
//                .build();
//
//        smsService.sendSMS(smsDetails);
//        transactionService.saveTransaction(debitTransactiondto);
//        transactionService.saveTransaction(creditTransactiondto);

        return Response.builder()
                .responseCode(ResponseUtils.SUCCESSFUL_TRANSACTION)
                .responseMessage(ResponseUtils.ACCOUNT_DEBITED_FOR_TRANSFER)
                .data(Data.builder()
                        .accountName(sendingUser.getFirstName() + " " + sendingUser.getOtherName() + " " + sendingUser.getLastName())
                        .accountBalance(sendingUser.getAccountBalance())
                        .accountNumber(sendingUser.getAccountNumber())
                        .build())
                .build();
    }
}
