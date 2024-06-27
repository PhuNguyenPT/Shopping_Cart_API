package com.example.shopping_cart.product;

import com.example.shopping_cart.category.Category;
import com.example.shopping_cart.category.CategoryRepository;
import com.example.shopping_cart.category.CategoryService;
import com.example.shopping_cart.file.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final FileService fileService;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;

    public Product findByName(String name) {
        return productRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Product " + name + " not found"));
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public ProductResponseDTO save(
            @NotNull
            ProductRequestDTO productRequestDTOS
    ) {

        // Check if the product already exists
        Product existingProduct = findByName(productRequestDTOS.name());
        if (existingProduct != null) {
            throw new EntityExistsException("Product already exists");
        }

        // Handle Existing Categories
        List<Category> existingCategories = new ArrayList<>();
        if (productRequestDTOS.categoryIds() != null &&
                !productRequestDTOS.categoryIds().isEmpty()) {
            existingCategories = categoryService.findAllByIdIn(productRequestDTOS.categoryIds());
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
        Set<Category> combinedCategories = new HashSet<>(existingCategories);
        combinedCategories.addAll(savedNewCategories);
        savedProduct.setCategories(combinedCategories.stream().toList());
        for (Category category : combinedCategories) {
            category.addProduct(savedProduct);
        }

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

        return productResponseDTO;
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

    public List<Product> findByNameContainingIgnoreCase(String name) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);
        if (products == null || products.isEmpty()) {
            throw new EntityNotFoundException("Product(s) " + name + " not found");
        }
        return products;
    }

    public List<ProductResponseDTO> findBy(String productName) {
        List<Product> products = findByNameContainingIgnoreCase(productName);
        return products.stream()
                        .map(ProductMapper::toProductResponseDTO)
                        .toList();
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Product with id " +
                                id + " not found"));
    }

    public Page<ProductResponseDTO> findByProductNameAndPage(
            String productName, Integer pageNumber, Integer pageSize
    ) {
        List<Product> products = findByNameContainingIgnoreCase(productName);
        List<ProductResponseDTO> productResponseDTOList = products.stream()
                .map(ProductMapper::toProductResponseDTO)
                .toList();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return new PageImpl<>(
                productResponseDTOList,
                pageable,
                productResponseDTOList.size()
        );
    }

    @Transactional
    public ResponseEntity<?> deleteBy(Long productId) {
        Product product = findById(productId);
        productRepository.deleteById(product.getId());
        return ResponseEntity.ok("Product with id " + productId + " is deleted successfully");
    }

    @Transactional
    public ProductResponseDTO updateFileByProductIdAndFileId(
            Long productId,
            Long fileId,
            MultipartFile multipartFile
    ) {
        Product product = findById(productId);
        FileResponseDTO fileResponseDTO = fileService.updateFile(multipartFile, product, fileId);
        fileResponseDTO.setMessage("Update files " + fileResponseDTO.getName() + " successfully");
//        URI location = ServletUriComponentsBuilder
//                .fromCurrentRequest()
//                .path("/{id}")
//                .buildAndExpand(fileResponseDTO.getId())
//                .toUri();
        return ProductMapper.toProductResponseDTOUpdate(product, fileResponseDTO);
    }

    @Transactional
    public ProductResponseDTO createProductFilesByProductId(
            Long productId,
            @NotNull List<MultipartFile> multipartFiles) {
        Product product = findById(productId);
        List<FileResponseDTO> fileResponseDTOList = fileService.saveFilesByProduct(product, multipartFiles);
        ProductResponseDTO productResponseDTO = ProductMapper.toProductResponseDTOCreateFiles(product, fileResponseDTOList);
        return productResponseDTO;
    }

    public ProductResponseDTO updateProductAttributes(
            Long id,
            @NotNull ProductUpdateDTO productUpdateDTO
    ) {
        Product product = findById(id);
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
        return productResponseDTO;
    }
}
