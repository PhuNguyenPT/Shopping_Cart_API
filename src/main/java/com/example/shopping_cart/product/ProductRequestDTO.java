package com.example.shopping_cart.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ProductRequestDTO(
        @NotNull(message = "Product name must not be null")
        @NotBlank(message = "Product name must not be blank")
        String name,
        @NotNull(message = "Product price must not be null")
        @DecimalMin(value = "0.01", message = "Product price must be greater than 0")
        Double price,
        @NotNull(message = "Product stock quantity must not be null")
        @Min(value = 0, message = "Product stock quantity must be greater than 0")
        Long stockQuantity,
        String description,
        List<Long> categoryIds,
        List<String> newCategoryNames,
        List<MultipartFile> files
) {}
