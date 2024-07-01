package com.example.shopping_cart.product;

import com.example.shopping_cart.category.CategoryMapper;
import com.example.shopping_cart.file.FileMapper;
import com.example.shopping_cart.file.FileResponseDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
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

    public static ProductResponseDTO toProductResponseDTOOrder(
            @NotNull Product product
    ) {
        return ProductResponseDTO.builder()
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .fileResponseDTOList(
                        Collections.singletonList(
                                product.getFiles().stream()
                                        .map(FileMapper::toFileResponseDTOShoppingCart)
                                        .findFirst()
                                        .orElseThrow(() -> new RuntimeException("Cannot find file(s) in the shopping cart product"))
                        )
                )
                .build();
    }

    public static ProductResponseDTO toProductResponseDTOCategory(
            @NotNull Product product
    ) {
        return ProductResponseDTO.builder()
                .id(product.getId())
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
                .build();
    }

    public static ProductResponseDTO toProductResponseDTOUpdate(
            @NotNull Product product,
            FileResponseDTO fileResponseDTO
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
                                .map(file -> FileMapper.toFileResponseDTOUpdateProduct(file, fileResponseDTO))
                                .toList()
                )
                .categoryResponseDTOList(
                        product.getCategories().stream()
                                .map(CategoryMapper::toCategoryResponseDTO)
                                .toList()
                )
                .build();
    }

    public static ProductResponseDTO toProductResponseDTOCreateFiles(
            @NotNull Product product,
            List<FileResponseDTO> fileResponseDTOList
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
                                .map(file -> FileMapper.toFileResponseDTOSaveProductFiles(file, fileResponseDTOList))
                                .toList()
                )
                .categoryResponseDTOList(
                        product.getCategories().stream()
                                .map(CategoryMapper::toCategoryResponseDTO)
                                .toList()
                )
                .build();
    }

}
//
//package com.example.shopping_cart.product;
//
//import com.example.shopping_cart.category.CategoryMapper;
//import com.example.shopping_cart.file.FileMapper;
//import com.example.shopping_cart.file.FileResponseDTO;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.ZonedDateTime;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class ProductMapper {
//
//    public static Product toProduct(@NotNull ProductRequestDTO productRequestDTO) {
//        return Product.builder()
//                .name(productRequestDTO.name())
//                .price(productRequestDTO.price())
//                .description(productRequestDTO.description())
//                .stockQuantity(productRequestDTO.stockQuantity())
//                .build();
//    }
//
//    private static ZonedDateTime safeZonedDateTime(LocalDateTime dateTime, ZoneId zoneId) {
//        return (dateTime != null && zoneId != null) ? ZonedDateTime.of(dateTime, zoneId) : null;
//    }
//
//    public static ProductResponseDTO toProductResponseDTO(@NotNull Product product) {
//        return ProductResponseDTO.builder()
//                .id(product.getId())
//                .name(product.getName())
//                .price(product.getPrice())
//                .stockQuantity(product.getStockQuantity())
//                .description(product.getDescription())
//                .createdDate(safeZonedDateTime(product.getCreatedDate(), product.getCreatedTimeZone()))
//                .lastModifiedDate(safeZonedDateTime(product.getLastModifiedDate(), product.getModifiedTimeZone()))
//                .fileResponseDTOList(product.getFiles().stream()
//                        .map(FileMapper::toFileResponseDTOSearch)
//                        .toList())
//                .categoryResponseDTOList(product.getCategories().stream()
//                        .map(CategoryMapper::toCategoryResponseDTO)
//                        .toList())
//                .build();
//    }
//
//    public static ProductResponseDTO toProductResponseDTOShoppingCart(@NotNull Product product) {
//        return ProductResponseDTO.builder()
//                .name(product.getName())
//                .price(product.getPrice())
//                .stockQuantity(product.getStockQuantity())
//                .description(product.getDescription())
//                .fileResponseDTOList(Collections.singletonList(
//                        product.getFiles().stream()
//                                .map(FileMapper::toFileResponseDTOShoppingCart)
//                                .findFirst()
//                                .orElseThrow(() -> new RuntimeException("Cannot find file(s) in the shopping cart product"))
//                ))
//                .categoryResponseDTOList(product.getCategories().stream()
//                        .map(CategoryMapper::toCategoryResponseDTO)
//                        .toList())
//                .build();
//    }
//
//    public static ProductResponseDTO toProductResponseDTOOrder(@NotNull Product product) {
//        return ProductResponseDTO.builder()
//                .name(product.getName())
//                .price(product.getPrice())
//                .description(product.getDescription())
//                .fileResponseDTOList(Collections.singletonList(
//                        product.getFiles().stream()
//                                .map(FileMapper::toFileResponseDTOShoppingCart)
//                                .findFirst()
//                                .orElseThrow(() -> new RuntimeException("Cannot find file(s) in the shopping cart product"))
//                ))
//                .build();
//    }
//
//    public static ProductResponseDTO toProductResponseDTOCategory(@NotNull Product product) {
//        return ProductResponseDTO.builder()
//                .id(product.getId())
//                .name(product.getName())
//                .price(product.getPrice())
//                .stockQuantity(product.getStockQuantity())
//                .description(product.getDescription())
//                .fileResponseDTOList(Collections.singletonList(
//                        product.getFiles().stream()
//                                .map(FileMapper::toFileResponseDTOShoppingCart)
//                                .findFirst()
//                                .orElseThrow(() -> new RuntimeException("Cannot find file(s) in the shopping cart product"))
//                ))
//                .build();
//    }
//
//    public static ProductResponseDTO toProductResponseDTOUpdate(@NotNull Product product, FileResponseDTO fileResponseDTO) {
//        return ProductResponseDTO.builder()
//                .id(product.getId())
//                .name(product.getName())
//                .price(product.getPrice())
//                .stockQuantity(product.getStockQuantity())
//                .description(product.getDescription())
//                .createdDate(safeZonedDateTime(product.getCreatedDate(), product.getCreatedTimeZone()))
//                .lastModifiedDate(safeZonedDateTime(product.getLastModifiedDate(), product.getModifiedTimeZone()))
//                .fileResponseDTOList(product.getFiles().stream()
//                        .map(file -> FileMapper.toFileResponseDTOUpdateProduct(file, fileResponseDTO))
//                        .toList())
//                .categoryResponseDTOList(product.getCategories().stream()
//                        .map(CategoryMapper::toCategoryResponseDTO)
//                        .toList())
//                .build();
//    }
//
//    public static ProductResponseDTO toProductResponseDTOCreateFiles(@NotNull Product product, List<FileResponseDTO> fileResponseDTOList) {
//        return ProductResponseDTO.builder()
//                .id(product.getId())
//                .name(product.getName())
//                .price(product.getPrice())
//                .stockQuantity(product.getStockQuantity())
//                .description(product.getDescription())
//                .createdDate(safeZonedDateTime(product.getCreatedDate(), product.getCreatedTimeZone()))
//                .lastModifiedDate(safeZonedDateTime(product.getLastModifiedDate(), product.getModifiedTimeZone()))
//                .fileResponseDTOList(product.getFiles().stream()
//                        .map(file -> FileMapper.toFileResponseDTOSaveProductFiles(file, fileResponseDTOList))
//                        .toList())
//                .categoryResponseDTOList(product.getCategories().stream()
//                        .map(CategoryMapper::toCategoryResponseDTO)
//                        .toList())
//                .build();
//    }
//}