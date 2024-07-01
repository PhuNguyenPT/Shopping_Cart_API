package com.example.shopping_cart.category;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category1;
    private Category category2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        category1 = new Category();
        category1.setName("Electronics");
        category1.setId(1L);
//        entityManager.persist(category1);

        category2 = new Category();
        category2.setName("Books");
        category2.setId(2L);
//        entityManager.persist(category2);
    }

    @Test
    public void testFindAllByIdIn() {
        List<Long> categoryIds = Arrays.asList(1L, 2L);
        List<Category> mockCategories = Arrays.asList(category1, category2);

        when(categoryRepository.findAllByIdIn(categoryIds)).thenReturn(mockCategories);

        List<Category> result = categoryService.findAllByIdIn(categoryIds);
        assertEquals(2, result.size());
        assertEquals("Electronics", result.get(0).getName());
        assertEquals("Books", result.get(1).getName());

        verify(categoryRepository, times(1)).findAllByIdIn(categoryIds);
    }

    @Test
    public void testFindAllEmpty() {
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class, () -> {
            categoryService.findAll();
        });

        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void testFindByNameOrElseCreateNewCategory() {
        // Mock behavior of categoryRepository.findByName
        when(categoryRepository.findByName("Electronics")).thenReturn(category1);
        when(categoryRepository.findByName("Furniture")).thenReturn(null);

        List<String> categoryNames = Arrays.asList("Electronics", "Furniture");

        List<Category> categories = categoryService.findByNameOrElseCreateNewCategory(categoryNames);
        assertEquals(2, categories.size());
        assertEquals("Electronics", categories.get(0).getName());
        assertEquals("Furniture", categories.get(1).getName());
    }

    @Test
    public void testSaveAll() {
        List<Category> categories = Arrays.asList(category1, category2);

        when(categoryRepository.saveAll(categories)).thenReturn(categories);

        List<Category> result = categoryService.saveAll(categories);
        assertEquals(2, result.size());
        assertEquals("Electronics", result.get(0).getName());
        assertEquals("Books", result.get(1).getName());

        verify(categoryRepository, times(1)).saveAll(categories);
    }

    @Test
    public void testFindById() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));

        Category result = categoryService.findById(1L);
        assertEquals("Electronics", result.getName());

        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    public void testFilterAllProductsByCategoryIdIn() {
        CategoryRequestDTO categoryRequestDTO1 = CategoryRequestDTO.builder()
                .categoryId(1L)
                .name("Electronics")
                .build();

        CategoryRequestDTO categoryRequestDTO2 = CategoryRequestDTO.builder()
                .categoryId(2L)
                .name("Books")
                .build();

//        CategoryRequestFilterDTO categoryRequestFilterDTO = CategoryRequestFilterDTO.builder()
//                .categoryRequestDTOList(Arrays.asList(categoryRequestDTO1, categoryRequestDTO2))
//                .pageNumber(0)
//                .pageSize(10)
//                .build();

        List<Long> idList = Arrays.asList(1L, 2L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category2));

        CategoryResponseDTOFilter result = categoryService.filterAllProductsByCategoryIdIn(idList, 1, 10);
        // Add assertions to check the contents of 'result'

        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).findById(2L);
    }
}
