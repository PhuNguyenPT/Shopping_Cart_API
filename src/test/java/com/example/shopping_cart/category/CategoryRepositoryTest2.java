package com.example.shopping_cart.category;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class CategoryRepositoryTest2 {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testFindAllByIdIn() {
        // Given
        Category category1 = new Category();
        category1.setName("Electronics");
        categoryRepository.save(category1);

        Category category2 = new Category();
        category2.setName("Books");
        categoryRepository.save(category2);

        List<Long> categoryIds = List.of(category1.getId(), category2.getId());

        // When
        List<Category> categories = categoryRepository.findAllByIdIn(categoryIds);

        // Then
        assertThat(categories).hasSize(2).extracting(Category::getName).containsExactlyInAnyOrder("Electronics", "Books");
    }

    @Test
    public void testFindAllByIdIn_NotFound() {
        // Given
        List<Long> categoryIds = List.of(999L);

        // When
        List<Category> categories = categoryRepository.findAllByIdIn(categoryIds);

        // Then
        assertThat(categories).isEmpty();
    }

    @Test
    public void testFindByName() {
        // Given
        Category category = new Category();
        category.setName("Electronics");
        categoryRepository.save(category);

        // When
        Category foundCategory = categoryRepository.findByName("Electronics");

        // Then
        assertThat(foundCategory).isNotNull();
        assertThat(foundCategory.getName()).isEqualTo("Electronics");
    }

    @Test
    public void testFindByName_NotFound() {
        // When / Then
        assertThrows(org.springframework.dao.EmptyResultDataAccessException.class, () -> {
            categoryRepository.findByName("NonExistentCategory");
        });
    }
}