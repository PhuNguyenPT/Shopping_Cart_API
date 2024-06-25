package com.example.shopping_cart.product_quantity;

import com.example.shopping_cart.product.ProductMapper;
import com.example.shopping_cart.product.ProductResponseDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class ProductQuantityMapper {

    public static ProductQuantityResponseDTO toProductQuantityResponseDTOSave(
            @NotNull ProductQuantity productQuantity
    ) {
        return ProductQuantityResponseDTO.builder()
                .productQuantityId(productQuantity.getId())
                .shoppingCartId(productQuantity.getShoppingCart().getId())
                .productId(productQuantity.getProduct().getId())
                .quantity(productQuantity.getQuantity())
                .totalAmount(productQuantity.getTotalAmount())
                .productResponseDTO(
                        ProductMapper.toProductResponseDTOShoppingCart(productQuantity.getProduct())
                )
                .build();
    }

    public static ProductQuantityResponseDTO toProductQuantityResponseDTOSaveOrder(
            @NotNull ProductQuantity productQuantity
    ) {
        return ProductQuantityResponseDTO.builder()
                .orderId(productQuantity.getOrder().getId())
                .productQuantityId(productQuantity.getId())
                .productId(productQuantity.getProduct().getId())
                .quantity(productQuantity.getQuantity())
                .totalAmount(productQuantity.getTotalAmount())
                .productResponseDTO(
                        ProductMapper.toProductResponseDTOOrder(productQuantity.getProduct())
                )
                .build();
    }
}
