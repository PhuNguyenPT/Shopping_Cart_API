package com.example.shopping_cart.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/products")
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> upload(
            @ModelAttribute @Valid
            ProductRequestDTO productRequestDTO
    ) {
        return productService.save(productRequestDTO);
    }

    @GetMapping("/search/{product-name}")
    public ResponseEntity<?> searchBy(
            @PathVariable(value = "product-name", required = false)
            String productName
    ) {
        return productService.findBy(productName);
    }

    @DeleteMapping("/delete/{product-id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> delete(
            @PathVariable(value = "product-id")
            @NotNull(message = "Product id must not be null")
            Long id
    ) {
        return productService.deleteBy(id);
    }

    @PostMapping("/{productId}/files")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createProductFiles(
            @PathVariable Long productId,
            @RequestParam("files")
            @NotNull(message = "Please provide at least one file")
            List<MultipartFile> multipartFiles) {
        return productService.createProductFilesByProductId(productId, multipartFiles);
    }

    @PutMapping("/update/{product-id}/files/{file-id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateProductFileByProductId(
            @PathVariable(value = "product-id") Long productId,
            @PathVariable(value = "file-id") Long fileId,
            @RequestParam("files")
            @NotNull(message = "Please provide a file to update")
            MultipartFile multipartFile
    ) {
        return productService.updateFilesByProductId(productId, fileId, multipartFile);
    }

    @PatchMapping("/update/{product-id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateProductAttributes(
            @PathVariable(value = "product-id") Long id,
            @ModelAttribute @Valid ProductUpdateDTO productUpdateDTO
    ) {
        return productService.updateProductAttributes(id, productUpdateDTO);
    }
}
