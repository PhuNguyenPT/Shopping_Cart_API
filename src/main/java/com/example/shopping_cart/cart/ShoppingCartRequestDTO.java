package com.example.shopping_cart.cart;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@Builder
public class ShoppingCartRequestDTO {
    @NotNull(value = "Product id must not be null")
    @Min(value = 1, message = "Product id must be greater than 0")
    private final Long productId;

    @NotNull(value = "Product quantity must not be null")
    @Min(value = 1, message = "Product quantity must be greater than 0")
    private final Long quantity;
}
