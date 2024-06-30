package com.example.shopping_cart.category;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.shopping_cart.product.Product;
import com.example.shopping_cart.product.ProductResponseDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CategoryMapperTest2 {

    @Test
    public void testToCategoryResponseDTO() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        CategoryResponseDTO dto = CategoryMapper.toCategoryResponseDTO(category);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Electronics");
    }

    @Test
    public void testToCategoryResponseDTOFilter() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");
        Product product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        category.setProducts(List.of(product));

        CategoryResponseDTO dto = CategoryMapper.toCategoryResponseDTOFilter(category);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Electronics");
        assertThat(dto.getProductResponseDTOList()).hasSize(1);
        assertThat(dto.getProductResponseDTOList().get(0).getName()).isEqualTo("Laptop");
    }
}