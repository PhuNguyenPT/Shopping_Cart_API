package com.example.shopping_cart.category;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

//    public static List<CategoryResponseDTO> toCategoryResponseDTOList(
//            @NotNull List<Category> categories
//    ) {
//        List<CategoryResponseDTO> categoryResponseDTOList = new ArrayList<>();
//        return categoryResponseDTOList = categories.stream()
//                .map(CategoryMapper::toCategoryResponseDTO)
//                .toList();
//    }
}
