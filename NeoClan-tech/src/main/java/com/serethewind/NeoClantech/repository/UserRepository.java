package com.serethewind.NeoClantech.repository;

import com.serethewind.NeoClantech.dto.Response;
import com.serethewind.NeoClantech.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);

    Boolean existsByAccountNumber(String accountNumber);

    User findByAccountNumber(String accountNumber);
}
