package com.example.shopping_cart.role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyRoleRepository extends JpaRepository<MyRole, Long> {
    Optional<MyRole> findByAuthority(String authority);
}
