package com.example.shopping_cart.transaction;

import com.example.shopping_cart.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findByOrder(Order order);
}
