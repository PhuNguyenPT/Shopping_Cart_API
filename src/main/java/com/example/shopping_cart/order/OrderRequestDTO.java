package com.example.shopping_cart.order;

import com.example.shopping_cart.address.AddressRequestDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@Builder
public class OrderRequestDTO {
    private final String orderInfo;
    private final String anotherField;

    @NotNull(message = "Phone number must not be null")
    private final BigInteger phoneNumber;
    private final AddressRequestDTO addressRequestDTO;
}
