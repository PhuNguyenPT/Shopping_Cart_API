package com.example.shopping_cart.cart;

import com.example.shopping_cart.product_quantity.ProductQuantityResponseDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
public class ShoppingCartResponseDTOFind {
    private String message;
    private final Long cartId;
    private final UUID userId;
    private final BigDecimal totalAmount;
    private final Page<ProductQuantityResponseDTO> productQuantityResponseDTOPage;
}
