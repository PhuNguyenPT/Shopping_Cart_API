package com.example.shopping_cart.user;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MyUserRepository extends JpaRepository<MyUser, UUID> {
    Optional<MyUser> findByEmail(String email);
    @NotNull
    Optional<MyUser> findById(@NotNull UUID id);
}
