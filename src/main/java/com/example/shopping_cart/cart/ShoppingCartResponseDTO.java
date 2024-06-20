package com.example.shopping_cart.cart;

import com.example.shopping_cart.product_quantity.ProductQuantityResponseDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class ShoppingCartResponseDTO {
    private String message;
    private final Long cartId;
    private final UUID userId;
    private final BigDecimal totalAmount;
    private final List<ProductQuantityResponseDTO> productQuantityResponseDTOList;
}
