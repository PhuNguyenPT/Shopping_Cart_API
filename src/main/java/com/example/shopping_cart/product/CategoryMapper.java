package com.example.shopping_cart.product;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class CategoryMapper {

    public static CategoryResponseDTO toCategoryResponseDTO(
            @NotNull Category category
    ) {
        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
