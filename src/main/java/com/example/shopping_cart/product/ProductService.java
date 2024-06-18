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

    @Transactional
    public ResponseEntity<?> save(
            @NotNull
            ProductRequestDTO productRequestDTOS,
            @NotNull List<MultipartFile> multipartFiles) {
        Product product = ProductMapper.toProduct(productRequestDTOS);
        Product savedProduct = productRepository.save(product);

        List<File> files = multipartFiles.stream()
                .filter(multipartFile -> !multipartFile.isEmpty())
                .map(multipartFile -> FileMapper.toFileSave(multipartFile, savedProduct))
                .collect(Collectors.toList());
        List<File> savedFiles = fileRepository.saveAll(files);
        savedFiles.forEach(savedProduct::addFile);

        ProductResponseDTO productResponseDTO = ProductMapper.toProductResponseDTO(savedProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponseDTO);
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

    public ResponseEntity<?> deleteBy(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(EntityNotFoundException::new);
        productRepository.deleteById(product.getId());
        return ResponseEntity.ok("Product with id " + productId + " is deleted successfully");
    }

    public ResponseEntity<?> updateFilesByProductId(
            Long productId,
            Long fileId,
            MultipartFile multipartFile
    ) {
        Product product = productRepository.findById(productId)
                .orElseThrow(EntityNotFoundException::new);
        FileResponseDTO fileResponseDTO = fileService.updateFile(multipartFile, product, fileId);
//        URI location = ServletUriComponentsBuilder
//                .fromCurrentRequest()
//                .path("/{id}")
//                .buildAndExpand(fileResponseDTO.getId())
//                .toUri();
        return ResponseEntity.status(HttpStatus.CREATED).body(fileResponseDTO);
    }

    public ResponseEntity<?> createProductFilesByProductId(
            Long productId,
            List<MultipartFile> multipartFiles) {
        Product product = productRepository.findById(productId)
                .orElseThrow(EntityNotFoundException::new);
        return fileService.saveFilesByProduct(product, multipartFiles);
    }
}
