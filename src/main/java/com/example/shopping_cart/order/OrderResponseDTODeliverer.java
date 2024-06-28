package com.example.shopping_cart.order;

import com.example.shopping_cart.address.AddressResponseDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class OrderResponseDTODeliverer {
    private String message;
    private final Long id;
    private final String fullName;
    private final BigDecimal totalAmount;
    private final String status;
    private final LocalDateTime deliveryDate;
    private final String orderInfo;

    private final BigInteger phoneNumber;
    private final AddressResponseDTO addressResponseDTO;
}
