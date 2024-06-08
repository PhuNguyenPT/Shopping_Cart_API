package com.example.shopping_cart.file;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface FileRepository extends JpaRepository<File, Integer> {
    Optional<File> findByNameContainingIgnoreCase(String name);
    Long deleteByName(String name);
}
