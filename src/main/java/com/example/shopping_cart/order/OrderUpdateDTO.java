package com.example.shopping_cart.order;

import com.example.shopping_cart.address.AddressRequestDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class OrderUpdateDTO {
    private final String message;
    private final String orderInfo;
    private final String anotherField;

    private final BigInteger phoneNumber;
    private final AddressRequestDTO addressRequestDTO;
}
