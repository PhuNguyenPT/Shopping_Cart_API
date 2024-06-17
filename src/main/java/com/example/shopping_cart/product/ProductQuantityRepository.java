package com.example.shopping_cart.product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductQuantityRepository extends JpaRepository<ProductQuantity, Long> {
}
