package com.example.shopping_cart.category;

import com.example.shopping_cart.product.Product;
import com.example.shopping_cart.product.ProductMapper;
import com.example.shopping_cart.product.ProductResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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

    @Transactional
    public List<Category> saveAll(List<Category> categories) {
        return categoryRepository.saveAll(categories);
    }

//    public List<CategoryResponseDTO> filterAllProductsByCategoryIdIn(
//            @NotNull List<CategoryRequestDTO> categoryRequestDTOList
//    ) {
//         List<Category> categories = categoryRequestDTOList.stream()
//                .map(CategoryRequestDTO::getCategoryId)
//                .distinct()
//                .map(this::findById)
//                .toList();
//        return categories.stream()
//                .map(CategoryMapper::toCategoryResponseDTOFilter)
//                .toList();
//    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find category with id " + id));
    }

    public CategoryResponseDTOFilter filterAllProductsByCategoryIdIn(
            @NotNull CategoryRequestFilterDTO categoryRequestFilterDTO
    ) {
        // Handle page
        Pageable pageable = PageRequest.of(
                categoryRequestFilterDTO.getPageNumber(),
                categoryRequestFilterDTO.pageSize);
        Set<Category> categories = categoryRequestFilterDTO
                .getCategoryRequestDTOList().stream()
                .map(CategoryRequestDTO::getCategoryId)
                .map(this::findById)
                .collect(Collectors.toSet());

        // Set of unique products
        Set<Product> uniqueProducts = new HashSet<>();

        // Add all the products of each categories
        categories.forEach(category -> {
            List<Product> products = category.getProducts();
            uniqueProducts.addAll(products);
        });

        // Map unique products to response dto list
        List<ProductResponseDTO> productResponseDTOList = uniqueProducts.stream()
                .map(ProductMapper::toProductResponseDTOCategory)
                .toList();

        // Map of id, category names
        Map<Long, String> namesMap = categories.stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));

        // Page of products dto
        Page<ProductResponseDTO> productsDTOPage = new PageImpl<>(
                productResponseDTOList, pageable, productResponseDTOList.size()
        );

        return CategoryMapper.toCategoryResponseDTOFilter(
                productsDTOPage, namesMap
        );
    }
}
