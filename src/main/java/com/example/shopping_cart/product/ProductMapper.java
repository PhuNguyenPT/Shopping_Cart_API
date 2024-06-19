package com.example.shopping_cart.product;

import com.example.shopping_cart.category.CategoryMapper;
import com.example.shopping_cart.file.FileMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Optional;

@Service
public class ProductMapper {

    public static Product toProduct(
            @NotNull ProductRequestDTO productRequestDTO) {
        return Product.builder()
                .name(productRequestDTO.name())
                .price(productRequestDTO.price())
                .description(productRequestDTO.description())
                .stockQuantity(productRequestDTO.stockQuantity())
                .build();
    }

    public static ProductResponseDTO toProductResponseDTO(
            @NotNull Product product
    ) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .description(product.getDescription())
                .createdDate(
                        ZonedDateTime.of(
                                product.getCreatedDate(),
                                product.getCreatedTimeZone()
                        )
                )
                .lastModifiedDate(
                        Optional.ofNullable(product.getLastModifiedDate())
                                .map(lastModifiedDate -> {
                                            if (product.getModifiedTimeZone() != null) {
                                                return ZonedDateTime.of(
                                                        lastModifiedDate,
                                                        ZoneId.of(String.valueOf(product.getModifiedTimeZone()))
                                                );
                                            }
                                            return null;
                                        }
                                )
                                .orElse(null)
                )
                .fileResponseDTOList(
                        product.getFiles().stream()
                                .map(FileMapper::toFileResponseDTOSearch)
                                .toList()
                )
                .categoryResponseDTOList(
                        product.getCategories().stream()
                                .map(CategoryMapper::toCategoryResponseDTO)
                                .toList()
                )
                .build();
    }
    public static ProductResponseDTO toProductResponseDTOShoppingCart(
            @NotNull Product product
    ) {
        return ProductResponseDTO.builder()
                .name(product.getName())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .description(product.getDescription())
                .fileResponseDTOList(
                        Collections.singletonList(
                                        product.getFiles().stream()
                                                .map(FileMapper::toFileResponseDTOShoppingCart)
                                                .findFirst()
                                                .orElseThrow(() -> new RuntimeException("Cannot find file(s) in the shopping cart product"))
                        )
                )
                .categoryResponseDTOList(
                        product.getCategories().stream()
                                .map(CategoryMapper::toCategoryResponseDTO)
                                .toList()
                )
                .build();
    }
}
