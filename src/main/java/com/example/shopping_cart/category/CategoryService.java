package com.example.shopping_cart.category;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> findAllByIdIn(List<Long> categoryIds) {
        List<Category> existingCategories = categoryRepository.findAllByIdIn(categoryIds);
        List<Long> notFoundCategoryIds = categoryIds.stream()
                .filter(id -> existingCategories.stream().noneMatch(category -> category.getId().equals(id)))
                .toList();
        if (!notFoundCategoryIds.isEmpty()) {
            throw new EntityNotFoundException("Categories not found for IDs: " + notFoundCategoryIds);
        }
        return existingCategories;
    }


    public ResponseEntity<?> findAll() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new EntityNotFoundException("Categories are emtpy");
        }
        List<CategoryResponseDTO> categoryResponseDTOList = categories.stream()
                .map(CategoryMapper::toCategoryResponseDTO)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(categoryResponseDTOList);
    }

    public List<Category> findByNameOrElseCreateNewCategory(
            @NotNull List<String> categoryNames
    ) {
        List<Category> categories = new ArrayList<>();
        for (String name : categoryNames) {
            Category category = categoryRepository.findByName(name);
            if (category == null) {
                category = Category.builder()
                        .name(name)
                        .build();

            }
            categories.add(category);
        }
        return categories;
    }

    public List<Category> saveAll(List<Category> categories) {
        return categoryRepository.saveAll(categories);
    }
}
