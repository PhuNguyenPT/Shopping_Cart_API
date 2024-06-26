package com.example.shopping_cart.product;

import com.example.shopping_cart.category.CategoryResponseDTO;
import com.example.shopping_cart.file.FileResponseDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ProductResponseDTO {
    private String message;
    private final Long id;
    private final String name;
    private final Double price;
    private final Long stockQuantity;
    private final String description;
    private final ZonedDateTime createdDate;
    private final ZonedDateTime lastModifiedDate;
    private final List<CategoryResponseDTO> categoryResponseDTOList;
    private final List<FileResponseDTO> fileResponseDTOList;
}
