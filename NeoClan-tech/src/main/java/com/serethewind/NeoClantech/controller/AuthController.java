package com.serethewind.NeoClantech.controller;

import com.serethewind.NeoClantech.dto.LoginDto;
import com.serethewind.NeoClantech.dto.RegisterDto;
import com.serethewind.NeoClantech.dto.Response;
import com.serethewind.NeoClantech.dto.UserRequest;
import com.serethewind.NeoClantech.entity.RoleEntity;
import com.serethewind.NeoClantech.entity.TokenEntity;
import com.serethewind.NeoClantech.entity.TokenType;
import com.serethewind.NeoClantech.entity.User;
import com.serethewind.NeoClantech.repository.RoleRepository;
import com.serethewind.NeoClantech.repository.TokenRepository;
import com.serethewind.NeoClantech.repository.UserRepository;
import com.serethewind.NeoClantech.securityConfig.JWTService;
import com.serethewind.NeoClantech.service.UserService;
import com.serethewind.NeoClantech.util.ResponseUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api/v1/auth")
@AllArgsConstructor
public class AuthController {
    private UserService userService;
    private AuthenticationManager authenticationManager;
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JWTService jwtService;
    private TokenRepository tokenRepository;


    @PostMapping("/register")
    public Response register(@RequestBody UserRequest userRequest){
        return userService.registerUser(userRequest);

//        if (userRepository.existsByUsernameOrEmail(userRequest.getUsername(), userRequest.getEmail())){
//            return new ResponseEntity<>("Username or Email is already taken", HttpStatus.BAD_REQUEST);
//        }
//
//        RoleEntity role = roleRepository.findByRolename("ROLE_USER").orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//
//       User user = User.builder()
//               .firstName(userRequest.getFirstName())
//               .lastName(userRequest.getLastName())
//               .otherName(userRequest.getOtherName())
//               .gender(userRequest.getGender())
//               .address(userRequest.getAddress())
//               .stateOfOrigin(userRequest.getStateOfOrigin())
//               .accountNumber(ResponseUtils.generateAccountNumber(ResponseUtils.lengthOfAccountNumber))
//               .accountBalance(userRequest.getAccountBalance())
//               .email(userRequest.getEmail())
//               .phoneNumber(userRequest.getPhoneNumber())
//               .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
//               .status("ACTIVE")
//               .dateOfBirth(userRequest.getDateOfBirth())
//               .build();
//
//        userRepository.save(user);
//
//        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        Authentication authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
//        return new ResponseEntity<>("User logged in successfully", HttpStatus.OK);




        String token = jwtService.generateToken(authentication.getName());
        User user = userRepository.findUserByUsername(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("user not found"));
        revokeValidTokens(user);
        TokenEntity tokenEntity = TokenEntity.builder()
                .user(user)
                .token(token)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(tokenEntity);

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    private void revokeValidTokens(User users) {
        List<TokenEntity> tokenEntityList = tokenRepository.findAllValidTokensByUser(users.getId());
        if (tokenEntityList.isEmpty())
            return;
        tokenEntityList.forEach(t -> {
            t.setRevoked(true);
            t.setExpired(true);
        });
        tokenRepository.saveAll(tokenEntityList);
    }

    @GetMapping("logout")
    public String logout(){
        return "User logged out";
    }
}
