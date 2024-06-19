package com.example.shopping_cart.product_quantity;

import com.example.shopping_cart.product.ProductResponseDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ProductQuantityResponseDTO {
    private final Long productQuantityId;
    private final Long productId;
    private final Long shoppingCartId;
    private final Long orderId;
    private final Long quantity;
    private final BigDecimal totalAmount;
    private final ProductResponseDTO productResponseDTO;
}
