package com.example.shopping_cart.order;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;


@Repository
@Transactional
public interface OrderRepository extends JpaRepository<Order, Long>{

}
