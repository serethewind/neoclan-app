package com.serethewind.NeoClantech.repository;

import com.serethewind.NeoClantech.dto.Response;
import com.serethewind.NeoClantech.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);

    Boolean existsByAccountNumber(String accountNumber);

    User findByAccountNumber(String accountNumber);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findUserByUsername(String username);
    Boolean existsByUsernameOrEmail(String username, String email);
}
