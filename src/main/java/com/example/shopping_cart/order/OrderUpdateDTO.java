package com.example.shopping_cart.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class OrderUpdateDTO {
    private final String message;
    private final String status;
    private final LocalDateTime deliveryDate;
    private final String orderInfo;
    private final String anotherField;
}
