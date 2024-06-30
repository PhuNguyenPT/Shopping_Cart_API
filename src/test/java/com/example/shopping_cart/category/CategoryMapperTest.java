package com.example.shopping_cart.category;

import com.example.shopping_cart.file.File;
import com.example.shopping_cart.product.Product;
import com.example.shopping_cart.product.ProductMapper;
import com.example.shopping_cart.product.ProductResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Base64;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CategoryMapperTest {

    private CategoryMapper mapper;
    private Category category;

    @BeforeEach
    void setUp() {
        mapper = new CategoryMapper();
    }

    @Test
    void testToCategoryResponseDTO() {
        category = new Category();
        category.setId(1L);
        category.setName("Category");

        CategoryResponseDTO categoryResponseDTO = mapper.toCategoryResponseDTO(category);
        assertEquals(category.getId(), categoryResponseDTO.getId());
        assertEquals(category.getName(), categoryResponseDTO.getName());
    }

    @Test
    void testToCategoryResponseDTOFilter() {
        File file1 = new File();
        file1.setId(1L);
        file1.setName("file1.txt");
        file1.setFileContent(Base64.getEncoder().encodeToString("content1".getBytes()).getBytes());

        File file2 = new File();
        file2.setId(2L);
        file2.setName("file2.txt");
        file2.setFileContent(Base64.getEncoder().encodeToString("content2".getBytes()).getBytes());


        // Create products
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product1");
        product1.setFiles(List.of(file1));

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product2");
        product2.setFiles(List.of(file2));

        // Convert products to DTOs
        ProductResponseDTO productResponseDTO1 = ProductMapper.toProductResponseDTOCategory(product1);
        ProductResponseDTO productResponseDTO2 = ProductMapper.toProductResponseDTOCategory(product2);
        List<ProductResponseDTO> productResponseDTOList = new ArrayList<>();
        productResponseDTOList.add(productResponseDTO1);
        productResponseDTOList.add(productResponseDTO2);

        // Create pageable and page of product DTOs
        Pageable pageable = PageRequest.of(0, 2);
        Page<ProductResponseDTO> productsDTOPage = new PageImpl<>(productResponseDTOList, pageable, productResponseDTOList.size());

        // Create namesMap
        Map<Long, String> namesMap = Map.of(
                1L, "Category 1",
                2L, "Category 2"
        );

        // Convert to CategoryResponseDTOFilter
        CategoryResponseDTOFilter categoryResponseDTOFilter = CategoryMapper.toCategoryResponseDTOFilter(productsDTOPage, namesMap);

        // Perform assertions
        assertEquals(productsDTOPage, categoryResponseDTOFilter.getProductsPage());
        assertEquals(namesMap, categoryResponseDTOFilter.getNamesMap());
    }
}