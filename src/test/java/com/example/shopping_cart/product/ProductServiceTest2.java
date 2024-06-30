package com.example.shopping_cart.product;

import com.example.shopping_cart.category.CategoryRepository;
import com.example.shopping_cart.category.CategoryService;
import com.example.shopping_cart.file.FileService;
import com.example.shopping_cart.sort.SortDirectionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ProductServiceTest2 {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private FileService fileService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllByNameAndPageAndDirectionAndSortAttribute() {
        // Arrange
        String productName = "test";
        int pageNumber = 0;
        int pageSize = 10;
        String direction = "asc";
        String sortAttribute = "price";

        ProductSort productSort = ProductSortMapper.toProductionSortDefaultCreatedDate(sortAttribute);
        Sort.Direction sortDirection = SortDirectionMapper.toSortDirectionDefaultDesc(direction);

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(99.99);
        products.add(product);

        when(productRepository.findByNameContainingIgnoreCase(anyString())).thenReturn(products);
        when(productRepository.findAll(any(Sort.class))).thenReturn(products);

        List<ProductResponseDTO> productResponseDTOList = products.stream()
                .map(ProductMapper::toProductResponseDTO)
                .toList();

        PageRequest pageable = PageRequest.of(pageNumber, pageSize);
        Page<ProductResponseDTO> expectedPage = new PageImpl<>(productResponseDTOList, pageable, productResponseDTOList.size());

        // Act
        Page<ProductResponseDTO> resultPage = productService.findAllByNameAndPageAndDirectionAndSortAttribute(
                productName, pageNumber, pageSize, direction, sortAttribute
        );

        // Assert
        assertEquals(expectedPage.getTotalElements(), resultPage.getTotalElements());
        assertEquals(expectedPage.getContent().size(), resultPage.getContent().size());
        assertEquals(expectedPage.getContent().get(0).getId(), resultPage.getContent().get(0).getId());
    }

    @Test
    void testFindById_ProductExists() {
        // Arrange
        Product product = new Product();
        product.setId(1L);
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        // Act
        Product foundProduct = productService.findById(1L);

        // Assert
        assertNotNull(foundProduct);
        assertEquals(1L, foundProduct.getId());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_ProductDoesNotExist() {
        // Arrange
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            productService.findById(1L);
        });

        assertEquals("Product with id 1 not found", exception.getMessage());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteBy_ProductExists() {
        // Arrange
        Product product = new Product();
        product.setId(1L);
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        // Act
        ResponseEntity<?> response = productService.deleteBy(1L);

        // Assert
        assertNotNull(response);
        assertEquals("Product with id 1 is deleted successfully", response.getBody());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteBy_ProductDoesNotExist() {
        // Arrange
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            productService.deleteBy(1L);
        });

        assertEquals("Product with id 1 not found", exception.getMessage());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(0)).deleteById(1L);
    }

    @Test
    void testUpdateProductAttributes_ProductExists() {
        // Arrange
        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setName("Old Name");
        existingProduct.setDescription("Old Description");

        ProductUpdateDTO updatedAttributes = ProductUpdateDTO.builder()
                .name("New Product Name")
                .price(99.99)
                .stockQuantity(100L)
                .description("New Product Description")
                .categoryIds(Arrays.asList(1L, 2L, 3L)) // assuming these category IDs exist
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);

        // Act
        productService.updateProductAttributes(1L, updatedAttributes);

        // Assert
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(existingProduct);

        assertEquals("New Product Name", existingProduct.getName());
        assertEquals("New Product Description", existingProduct.getDescription());
    }

    @Test
    void testUpdateProductAttributes_ProductDoesNotExist() {
        // Arrange
        ProductUpdateDTO updatedAttributes = ProductUpdateDTO.builder()
                .name("New Product Name")
                .price(99.99)
                .stockQuantity(100L)
                .description("New Product Description")
                .categoryIds(Arrays.asList(1L, 2L, 3L)) // assuming these category IDs exist
                .build();

        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            productService.updateProductAttributes(1L, updatedAttributes);
        });

        assertEquals("Product with id 1 not found", exception.getMessage());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    void testFindAllByDirectionAndSortAttribute_Ascending() {
        // Arrange
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product A");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product B");

        ProductSort sortAttribute = ProductSort.PRICE;
        List<Product> products = List.of(product1, product2);

        when(productRepository.findAll(Sort.by(Sort.Direction.ASC, "price"))).thenReturn(products);

        // Act
        List<Product> result = productService.findAllByDirectionAndSortAttribute(Sort.Direction.ASC, sortAttribute);

        // Assert
        verify(productRepository, times(1)).findAll(Sort.by(Sort.Direction.ASC, "price"));
        assertEquals(products, result);
    }

    @Test
    void testFindAllByDirectionAndSortAttribute_Descending() {
        // Arrange
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product A");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product B");

        ProductSort sortAttribute = ProductSort.PRICE;
        List<Product> products = List.of(product1, product2);

        when(productRepository.findAll(Sort.by(Sort.Direction.DESC, "price"))).thenReturn(products);

        // Act
        List<Product> result = productService.findAllByDirectionAndSortAttribute(Sort.Direction.DESC, sortAttribute);

        // Assert
        verify(productRepository, times(1)).findAll(Sort.by(Sort.Direction.DESC, "price"));
        assertEquals(products, result);
    }

    @Test
    void testFindAll() {
        // Arrange
        Product product1 = new Product();
        product1.setName("Product A");

        Product product2 = new Product();
        product2.setName("Product B");

        List<Product> products = List.of(product1, product2);

        when(productRepository.findAll(any(Sort.class))).thenReturn(products);

        // Act
        List<Product> result = productService.findAll(Sort.by(Sort.Direction.ASC, "name"));

        // Assert
        assertEquals(2, result.size());
        assertEquals("Product A", result.get(0).getName());
        assertEquals("Product B", result.get(1).getName());
    }
}