package com.example.shopping_cart.order;

import java.time.LocalDateTime;
import java.util.List;

import com.example.shopping_cart.product.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderRequestDTO {
    private final String status;
    private final String orderInfo;
    private final String anotherField;
}
