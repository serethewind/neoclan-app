package com.serethewind.NeoClantech.controller;

import com.serethewind.NeoClantech.dto.Response;
import com.serethewind.NeoClantech.dto.TransactionRequest;
import com.serethewind.NeoClantech.dto.TransferRequest;
import com.serethewind.NeoClantech.dto.UserRequest;
//import com.serethewind.NeoClantech.repository.UserRepository;
import com.serethewind.NeoClantech.service.UserService;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@OpenAPIDefinition(
        info = @Info(
                title = "Spring Boot Banking Application",
                description = "Spring Boot Banking Application REST APIs Documentation",
                version = "v1.0",
                contact = @Contact(
                        name = "Noah",
                        email = "osasereu@gmail.com",
                        url = "https://github.com/serethewind"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://github.com/serethewind"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Spring Boot Banking Application Documentation",
                url = "https://github.com/serethewind"
        )
)
@Tag(
        name = "User Account Service REST APIs/Endpoint",
        description = "Endpoints for manipulating User account"
)
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    public ResponseEntity<Response> registerUser(@RequestBody UserRequest userRequest){
        return  new ResponseEntity<>(userService.registerUser(userRequest), HttpStatus.CREATED)  ;
    }

    @GetMapping
    public ResponseEntity<List<Response>> fetchAllUsers(){
        return new ResponseEntity<>(userService.fetchAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> fetchSingleUser(@PathVariable("id") Long id) throws Exception{
        return new ResponseEntity<>(userService.fetchSingleUserById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateSingleUser(@PathVariable("id") Long id, @RequestBody UserRequest userRequest){
        return new ResponseEntity<>(userService.updateUserById(id, userRequest), HttpStatus.OK);
    }

    @GetMapping("/accountNumber")
    public Response balanceEnquiry(@RequestParam(name = "accountNumber") String accountNumber){
        return userService.balanceEnquiry(accountNumber);
    }

    @GetMapping("/accountName")
    public Response nameEnquiry(@RequestParam(name = "accountNumber") String accountNumber){
        return userService.nameEnquiry(accountNumber);
    }
    @PutMapping("/credit")
    public ResponseEntity<Response> credit (@RequestBody TransactionRequest transactionRequest){
        return new ResponseEntity<>(userService.creditRequest(transactionRequest), HttpStatus.OK);
    }

    @PutMapping("/debit")
    public ResponseEntity<Response> debit (@RequestBody TransactionRequest transactionRequest){
        return new ResponseEntity<>(userService.debitRequest(transactionRequest), HttpStatus.OK);
    }

    @PutMapping("/transfer")
    public ResponseEntity<Response> transfer(@RequestBody TransferRequest transferRequest){
        return new ResponseEntity<>(userService.transferRequest(transferRequest), HttpStatus.OK);
    }
}
