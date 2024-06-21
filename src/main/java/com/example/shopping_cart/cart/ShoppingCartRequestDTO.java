package com.example.shopping_cart.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class ShoppingCartRequestDTO {
    @NotNull(message = "Product id must not be null")
    @Min(value = 1, message = "Product id must be greater than 0")
    private final Long productId;

    @NotNull(message = "Product quantity must not be null")
    @Min(value = 1, message = "Product quantity must be greater than 0")
    private final Long quantity;
}
