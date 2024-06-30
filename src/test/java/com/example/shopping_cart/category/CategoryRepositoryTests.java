package com.example.shopping_cart.category;

import static org.junit.jupiter.api.Assertions.*;

import com.example.shopping_cart.category.Category;
import com.example.shopping_cart.category.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@DataJpaTest
@Transactional
public class CategoryRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    private Long category1Id;
    private Long category2Id;

    @BeforeEach
    public void setup() {
        Category category1 = new Category();
        category1.setName("Electronics");
        entityManager.persistAndFlush(category1);
        category1Id = category1.getId(); // Retrieve the generated ID

        Category category2 = new Category();
        category2.setName("Books");
        entityManager.persistAndFlush(category2);
        category2Id = category2.getId(); // Retrieve the generated ID
    }

    @Test
    public void testFindByName() {
        // When
        Category found = categoryRepository.findByName("Electronics");

        // Then
        assertEquals("Electronics", found.getName());
    }

    @Test
    void testFindAllByIdIn() {
        List<Long> ids = Arrays.asList(category1Id, category2Id); // Use the actual IDs

        // When
        List<Category> categories = categoryRepository.findAllByIdIn(ids);

        // Then
        assertEquals(2, categories.size());
        assertTrue(categories.stream().anyMatch(c -> c.getId().equals(category1Id)));
        assertTrue(categories.stream().anyMatch(c -> c.getId().equals(category2Id)));
    }
}