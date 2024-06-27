package com.example.shopping_cart.product_quantity;

import com.example.shopping_cart.cart.ShoppingCart;
import com.example.shopping_cart.product.Product;
import com.example.shopping_cart.product.ProductMapper;
import com.example.shopping_cart.product.ProductResponseDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

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

    public static ProductQuantity toProductQuantitySaveShoppingCart(
            ShoppingCart shoppingCart, Long quantity, Product product
    ) {
        return ProductQuantity.builder()
                .shoppingCart(shoppingCart)
                .quantity(quantity)
                .product(product)
                .build();
    }

    public static ProductQuantityResponseDTO toProductQuantityResponseDTO(
            @NotNull ProductQuantity productQuantity
    ) {
        return ProductQuantityResponseDTO.builder()
                .orderId(Optional.ofNullable(productQuantity.getOrder())
                        .map(order -> {
                                    if (order != null) {
                                        return order.getId();
                                    }
                                    return null;
                                }
                        )
                        .orElse(null))
                .productQuantityId(productQuantity.getId())
                .productId(Optional.ofNullable(productQuantity.getProduct())
                        .map(product -> {
                                    if (product != null) {
                                        return product.getId();
                                    }
                                    return null;
                                }
                        )
                        .orElse(null))
                .shoppingCartId(Optional.ofNullable(productQuantity.getShoppingCart())
                        .map(shoppingCart -> {
                                    if (shoppingCart != null) {
                                        return shoppingCart.getId();
                                    }
                                    return null;
                                }
                        )
                        .orElse(null))
                .quantity(productQuantity.getQuantity())
                .totalAmount(productQuantity.getTotalAmount())
                .productResponseDTO(
                        ProductMapper.toProductResponseDTOShoppingCart(productQuantity.getProduct())
                )
                .build();
    }
}
