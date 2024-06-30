package com.example.shopping_cart.product;

import com.example.shopping_cart.product.Product;
import com.example.shopping_cart.product.ProductRepository;
import com.example.shopping_cart.product.ProductService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    ProductService productService;

    @Mock
    ProductRepository productRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByName() {
        Product product = new Product();
        product.setName("Test Product");

        when(productService.findByName("Test Product")).thenReturn(product);

        Product found = productService.findByName("Test Product");

        assertEquals("Test Product", found.getName());
    }

    @Test
    void testFindByNameOrElseNull() {
        Product product = new Product();
        product.setName("Test Product");

        when(productService.findByNameOrElseNull("Test Product")).thenReturn(product);
        when(productService.findByNameOrElseNull("Nothing")).thenReturn(null);

        Product found = productService.findByNameOrElseNull("Test Product");

        assertEquals("Test Product", found.getName());
        assertNull(productService.findByNameOrElseNull("Nothing"));
    }

    @Test
    void testSave() {
        Product product = new Product();
        product.setName("Test Product");

        when(productService.save(product)).thenReturn(product);

        Product saved = productService.save(product);

        assertEquals("Test Product", saved.getName());
    }

    @Test
    void testSaveProductRequestDTO() {
        ProductRequestDTO productRequestDTO = new ProductRequestDTO("Test Product", 10.0, 10L, "Test Description", null, null, null);
        Product product = new Product();
        product.setName("Test Product");
        ProductResponseDTO productResponseDTO = new ProductResponseDTO("Product saved successfully", 1L, "Test Product", 10.0, 10L, "Test Description", null, null, null, null);

        when(productService.save(productRequestDTO)).thenReturn(productResponseDTO);

        ProductResponseDTO saved = productService.save(productRequestDTO);

        assertEquals("Product saved successfully", saved.getMessage());
    }

    @Test
    void testFindByNameContainingIgnoreCase() {
        Product product = new Product();
        product.setName("Test Product");

        Product product2 = new Product();
        product2.setName("Test Product 2");

        List<Product> productList = List.of(product, product2);

        when(productService.findByNameContainingIgnoreCase("Test")).thenReturn(productList);

        List<Product> found = productService.findByNameContainingIgnoreCase("Test");

        assertEquals(2, found.size());
        for (Product p : found) {
            assertTrue(p.getName().startsWith("Test"));
        }
    }

}

//    @Test
//    void findById() {
//    }
//
//    @Test
//    void deleteBy() {
//    }
//
//    @Test
//    void updateFileByProductIdAndFileId() {
//    }
//
//    @Test
//    void createProductFilesByProductId() {
//    }
//
//    @Test
//    void updateProductAttributes() {
//    }
//
//    @Test
//    void findAllByDirectionAndSortAttribute() {
//    }
//
//    @Test
//    void findAll() {
//    }
