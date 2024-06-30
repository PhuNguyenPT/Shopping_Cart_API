package com.example.shopping_cart.category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CategoryServiceIntegrationTest {

    @Autowired
    private CategoryService categoryService;

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
        List<Category> categories = categoryService.findAllByIdIn(categoryIds);

        // Then
        assertThat(categories).hasSize(2).extracting(Category::getName).containsExactlyInAnyOrder("Electronics", "Books");
    }

    @Test
    public void testFindAllByIdIn_NotFound() {
        // Given
        List<Long> categoryIds = List.of(999L);

        // Then
        assertThrows(EntityNotFoundException.class, () -> categoryService.findAllByIdIn(categoryIds));
    }

    @Test
    public void testFindAll() {
        // Given
        Category category1 = new Category();
        category1.setName("Electronics");
        categoryRepository.save(category1);

        Category category2 = new Category();
        category2.setName("Books");
        categoryRepository.save(category2);

        // When
        ResponseEntity<?> responseEntity = categoryService.findAll();

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<CategoryResponseDTO> responseBody = (List<CategoryResponseDTO>) responseEntity.getBody();
        assertThat(responseBody).hasSize(2).extracting(CategoryResponseDTO::getName).containsExactlyInAnyOrder("Electronics", "Books");
    }

    @Test
    public void testFindAll_Empty() {
        // When / Then
        assertThrows(EntityNotFoundException.class, () -> categoryService.findAll());
    }

    @Test
    public void testFindByNameOrElseCreateNewCategory() {
        // Given
        String categoryName1 = "Electronics";
        String categoryName2 = "Books";
        List<String> categoryNames = List.of(categoryName1, categoryName2);

        // When
        List<Category> categories = categoryService.findByNameOrElseCreateNewCategory(categoryNames);

        // Then
        assertThat(categories).hasSize(2).extracting(Category::getName).containsExactlyInAnyOrder("Electronics", "Books");
    }

    @Test
    public void testSaveAll() {
        // Given
        Category category1 = new Category();
        category1.setName("Electronics");

        Category category2 = new Category();
        category2.setName("Books");

        List<Category> categories = List.of(category1, category2);

        // When
        List<Category> savedCategories = categoryService.saveAll(categories);

        // Then
        assertThat(savedCategories).hasSize(2).extracting(Category::getName).containsExactlyInAnyOrder("Electronics", "Books");
    }

    @Test
    public void testFindById() {
        // Given
        Category category = new Category();
        category.setName("Electronics");
        categoryRepository.save(category);

        // When
        Category foundCategory = categoryService.findById(category.getId());

        // Then
        assertThat(foundCategory).isNotNull();
        assertThat(foundCategory.getName()).isEqualTo("Electronics");
    }

    @Test
    public void testFindById_NotFound() {
        // When / Then
        assertThrows(EntityNotFoundException.class, () -> categoryService.findById(999L));
    }
}