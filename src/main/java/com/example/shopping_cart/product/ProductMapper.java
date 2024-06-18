package com.example.shopping_cart.product;

import com.example.shopping_cart.file.FileMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

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
                        product.getFiles().stream().map(FileMapper::toFileResponseDTOSearch).collect(Collectors.toUnmodifiableList())
                )
                .build();
    }
}
