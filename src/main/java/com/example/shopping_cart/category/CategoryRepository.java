package com.example.shopping_cart.category;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByIdIn(List<Long> ids);
    @NotNull
    Category findByName(String name);
}
