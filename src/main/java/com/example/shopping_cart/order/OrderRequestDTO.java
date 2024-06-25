package com.example.shopping_cart.order;

import java.time.LocalDateTime;
import java.util.List;

import com.example.shopping_cart.product.Product;
import com.example.shopping_cart.user.Address;
import com.example.shopping_cart.user.AddressRequestDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderRequestDTO {
    private final String orderInfo;
    private final String anotherField;

    private final AddressRequestDTO addressRequestDTO;
}
