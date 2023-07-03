package com.serethewind.NeoClantech.repository;

import com.serethewind.NeoClantech.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByRolename(String rolename);
}
