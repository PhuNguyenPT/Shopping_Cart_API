package com.example.shopping_cart.category;

import com.example.shopping_cart.product.ProductMapper;
import com.example.shopping_cart.product.ProductResponseDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class    CategoryMapper {

    public static CategoryResponseDTO toCategoryResponseDTO(
            @NotNull Category category
    ) {
        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static CategoryResponseDTO toCategoryResponseDTOFilter(
            @NotNull Category category
    ) {
        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .productResponseDTOList(
                        category.getProducts().stream()
                                .map(ProductMapper::toProductResponseDTOCategory)
                                .toList()
                )
                .build();
    }

    public static CategoryResponseDTOFilter toCategoryResponseDTOFilter(
            @NotNull Page<ProductResponseDTO> productsDTOPage,
            @NotNull Map<Long, String> namesMap
            ) {
        return CategoryResponseDTOFilter.builder()
                .productsPage(productsDTOPage)
                .namesMap(namesMap)
                .build();
    }
}
