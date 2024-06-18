package com.example.shopping_cart.product;

import com.example.shopping_cart.file.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final FileService fileService;
    private final FileRepository fileRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;

    @Transactional
    public ResponseEntity<?> save(
            @NotNull
            ProductRequestDTO productRequestDTOS
    ) {

        // Check if the product already exists
        Product existingProduct = productRepository.findByName(productRequestDTOS.name());
        if (existingProduct != null) {
            ProductResponseDTO existingProductResponseDTO = ProductMapper.toProductResponseDTO(existingProduct);
            existingProductResponseDTO.setMessage("Product already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(existingProductResponseDTO);
        }

        // Handle Existing Categories
        List<Category> existingCategories;
        if (productRequestDTOS.categoryIds() != null &&
                !productRequestDTOS.categoryIds().isEmpty()) {
            existingCategories = categoryService.findAllByIdIn(productRequestDTOS.categoryIds());
        } else {
            existingCategories = new ArrayList<>();
        }

        // Create and save the product
        Product product = ProductMapper.toProduct(productRequestDTOS);
        Product savedProduct = productRepository.save(product);

        // Handle new Categories
        List<Category> newCategories = new ArrayList<>();
        if (productRequestDTOS.newCategoryNames() != null &&
                !productRequestDTOS.newCategoryNames().isEmpty()) {
            newCategories = categoryService.findByNameOrElseCreateNewCategory(productRequestDTOS.newCategoryNames());
        }

        // Save new categories
        List<Category> savedNewCategories = categoryService.saveAll(newCategories);

        // Handle combine categories
        List<Category> combinedCategories = new ArrayList<>(existingCategories);
        combinedCategories.addAll(savedNewCategories);
        product.setCategories(combinedCategories);

        // Handle file uploads
        List<File> savedFiles = new ArrayList<>();
        if (productRequestDTOS.files() != null &&
                !productRequestDTOS.files().isEmpty()) {
            savedFiles = fileService.saveAllFilesByProduct(productRequestDTOS.files(), savedProduct);
        }
        savedProduct.setFiles(savedFiles);

        // Prepare response
        ProductResponseDTO productResponseDTO = ProductMapper.toProductResponseDTO(savedProduct);
        productResponseDTO.setMessage(this.buildMessage(productResponseDTO));

        return ResponseEntity.status(HttpStatus.CREATED).body(productResponseDTO);
    }

    private String buildMessage(
            @NotNull ProductResponseDTO productResponseDTO
    ) {
        StringBuilder message = new StringBuilder("Save product successfully");

        if (productResponseDTO.getCategoryResponseDTOList() != null &&
                !productResponseDTO.getCategoryResponseDTOList().isEmpty()) {
            message.append(",with categories");
        }
        if (productResponseDTO.getFileResponseDTOList() != null &&
                !productResponseDTO.getFileResponseDTOList().isEmpty()) {
            message.append(",with files");
        }
        return String.valueOf(message);
    }

    public ResponseEntity<?> findBy(String productName) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(productName);
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        return ResponseEntity.ok(
                products.stream()
                        .map(ProductMapper::toProductResponseDTO)
                        .collect(Collectors.toUnmodifiableList())
        );
    }

    @Transactional
    public ResponseEntity<?> deleteBy(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(EntityNotFoundException::new);
        productRepository.deleteById(product.getId());
        return ResponseEntity.ok("Product with id " + productId + " is deleted successfully");
    }

    @Transactional
    public ResponseEntity<?> updateFilesByProductId(
            Long productId,
            Long fileId,
            MultipartFile multipartFile
    ) {
        Product product = productRepository.findById(productId)
                .orElseThrow(EntityNotFoundException::new);
        FileResponseDTO fileResponseDTO = fileService.updateFile(multipartFile, product, fileId);
        fileResponseDTO.setMessage("Update files " + fileResponseDTO.getName() + " successfully");
//        URI location = ServletUriComponentsBuilder
//                .fromCurrentRequest()
//                .path("/{id}")
//                .buildAndExpand(fileResponseDTO.getId())
//                .toUri();
        return ResponseEntity.status(HttpStatus.CREATED).body(fileResponseDTO);
    }

    @Transactional
    public ResponseEntity<?> createProductFilesByProductId(
            Long productId,
            List<MultipartFile> multipartFiles) {
        Product product = productRepository.findById(productId)
                .orElseThrow(EntityNotFoundException::new);
        return fileService.saveFilesByProduct(product, multipartFiles);
    }

    public ResponseEntity<?> updateProductAttributes(
            Long id,
            @NotNull ProductUpdateDTO productUpdateDTO
    ) {
        Product product = productRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        if (productUpdateDTO.getName() != null) {
            product.setName(productUpdateDTO.getName());
        }
        if (productUpdateDTO.getPrice() != null) {
            product.setPrice(productUpdateDTO.getPrice());
        }
        if (productUpdateDTO.getDescription() != null) {
            product.setDescription(productUpdateDTO.getDescription());
        }
        if (productUpdateDTO.getStockQuantity() != null) {
            product.setStockQuantity(productUpdateDTO.getStockQuantity());
        }
        if (productUpdateDTO.getCategoryIds() != null &&
                !productUpdateDTO.getCategoryIds().isEmpty()) {
            List<Category> savedCategories = categoryRepository.findAllByIdIn(productUpdateDTO.getCategoryIds());
            product.setCategories(savedCategories);
        }
        Product savedProduct = productRepository.save(product);
        ProductResponseDTO productResponseDTO = ProductMapper.toProductResponseDTO(savedProduct);
        productResponseDTO.setMessage("Update successfully");
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDTO);
    }
}
