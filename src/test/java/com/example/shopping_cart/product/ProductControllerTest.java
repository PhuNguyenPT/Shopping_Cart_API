package com.example.shopping_cart.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControllerTest {

    @Mock
    ProductService productService;

    @InjectMocks
    ProductController productController;

    MockMvc mockMvc;

    ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testUpload() throws Exception {
        ProductRequestDTO productRequestDTO = new ProductRequestDTO("Test Product", 10.0, 10L, "Test Description", null, null, null);
        ProductResponseDTO productResponseDTO = new ProductResponseDTO("Product saved successfully", 1L, "Test Product", 10.0, 10L, "Test Description", null, null, null, null);

        when(productService.save(any(ProductRequestDTO.class))).thenReturn(productResponseDTO);

        MockMultipartFile file = new MockMultipartFile("files", "test.txt", "text/plain", "test data".getBytes());

        mockMvc.perform(multipart("/products/upload")
                        .file(file)
                        .param("name", productRequestDTO.name())
                        .param("price", productRequestDTO.price().toString())
                        .param("stockQuantity", productRequestDTO.stockQuantity().toString())
                        .param("description", productRequestDTO.description())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }

    @Test
    public void testSearchByProductNameAndPageAndDirectionAndSortAttribute() throws Exception {
//        // Assuming that the productService returns some data
//        when(productService.findAllByNameAndPageAndDirectionAndSortAttribute(any(), any(), any(), any(), any()))
//                .thenReturn(new PageImpl<>(List.of(new ProductResponseDTO("Product found", 1L, "Test Product", 10.0, 10L, "Test Description", null, null, null, null))));

        mockMvc.perform(get("/products/search")
                        .param("product-name", "Test Product")
                        .param("page-size", "20")
                        .param("page-number", "1")
                        .param("sort", "created-date")
                        .param("direction", "desc"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDelete() throws Exception {
        // Assuming that the productService successfully deletes a product
//        when(productService.deleteBy(any())).thenReturn("Product with id 1 is deleted successfully");

        mockMvc.perform(delete("/products/delete/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateProductFiles() throws Exception {
        // Assuming that the productService successfully creates a product file
        when(productService.createProductFilesByProductId(any(), any()))
                .thenReturn(new ProductResponseDTO("Product file created successfully", 1L, "Test Product", 10.0, 10L, "Test Description", null, null, null, null));

        MockMultipartFile file = new MockMultipartFile("files", "test.txt", "text/plain", "test data".getBytes());

        mockMvc.perform(multipart("/products/1/files")
                        .file(file))
                .andExpect(status().isCreated());
    }

//    @Test
//    public void testUpdateProductFileByProductId() throws Exception {
//        // Assuming that the productService successfully updates a product file
////        when(productService.updateFileByProductIdAndFileId(any(), any(), any()))
////                .thenReturn(new ProductResponseDTO("Product file updated successfully", 1L, "Test Product", 10.0, 10L, "Test Description", null, null, null, null));
//
//        MockMultipartFile file = new MockMultipartFile("files", "test.txt", "text/plain", "test data".getBytes());
//
//        mockMvc.perform(multipart("/products/update/1/files/1")
//                        .file(file))
//                .andExpect(status().isCreated());
//    }
}