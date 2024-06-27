package com.example.shopping_cart.cart;

import com.example.shopping_cart.product.Product;
import com.example.shopping_cart.product_quantity.ProductQuantity;
import com.example.shopping_cart.product_quantity.ProductQuantityMapper;
import com.example.shopping_cart.product_quantity.ProductQuantityResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShoppingCartMapper {

    public static ShoppingCart toShoppingCart(
            @NotNull List<Product> products,
            @NotNull List<ShoppingCartRequestDTO> shoppingCartRequestDTOList,
            ShoppingCart savedShoppingCart) {

        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        for (ShoppingCartRequestDTO dto : shoppingCartRequestDTOList) {
            Product product = Optional.ofNullable(productMap.get(dto.getProductId()))
                    .orElseThrow(() -> new EntityNotFoundException("Product id mapping not found"));

            ProductQuantity productQuantity = ProductQuantity.builder()
                    .shoppingCart(savedShoppingCart)
                    .quantity(dto.getQuantity())
                    .product(product)
                    .build();

            boolean found = false;
            if (!savedShoppingCart.getQuantities().isEmpty()) {
                for (ProductQuantity existingProductQuantity : savedShoppingCart.getQuantities()) {
                    if (existingProductQuantity.getProduct().equals(product)) {
                        existingProductQuantity.setQuantity(dto.getQuantity());
                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                savedShoppingCart.getQuantities().add(productQuantity);
                product.getQuantities().add(productQuantity);
            }
        }

        return savedShoppingCart;
    }

    public static ShoppingCartResponseDTO toShoppingCartResponseDTO(
            @NotNull ShoppingCart shoppingCart
    ) {
        return ShoppingCartResponseDTO.builder()
                .message("Upload cart successfully")
                .cartId(shoppingCart.getId())
                .userId(shoppingCart.getUser().getId())
                .totalAmount(shoppingCart.getTotalAmount())
                .productQuantityResponseDTOList(
                        shoppingCart.getQuantities().stream()
                                .map(ProductQuantityMapper::toProductQuantityResponseDTOSave)
                                .toList()
                )
                .build();
    }

    public static ShoppingCartResponseDTOFind toShoppingCartResponseDTOFind(
            @NotNull ShoppingCart shoppingCart,
            Page<ProductQuantityResponseDTO> productQuantityResponseDTOPage
    ) {
        return ShoppingCartResponseDTOFind.builder()
                .message("Find cart successfully")
                .cartId(shoppingCart.getId())
                .userId(shoppingCart.getUser().getId())
                .totalAmount(shoppingCart.getTotalAmount())
                .productQuantityResponseDTOPage(
                        productQuantityResponseDTOPage
                )
                .build();
    }
}
