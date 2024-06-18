package com.example.shopping_cart.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/products")
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(
            @ModelAttribute @Valid
            ProductRequestDTO productRequestDTO,
            @RequestParam(value = "files", required = false)
            List<MultipartFile> multipartFiles
    ) {
        return productService.save(productRequestDTO, multipartFiles);
    }

    @GetMapping("/search/{product-name}")
    public ResponseEntity<?> searchBy(
            @PathVariable(value = "product-name", required = false)
            String productName
    ) {
        return productService.findBy(productName);
    }

    @DeleteMapping("/delete/{product-id}")
    public ResponseEntity<?> delete(
            @PathVariable(value = "product-id")
            @NotNull(message = "Product id must not be null")
            Long id
    ) {
        return productService.deleteBy(id);
    }

    @PostMapping("/{productId}/files")
    public ResponseEntity<?> createProductFiles(
            @PathVariable Long productId,
            @RequestParam("files") List<MultipartFile> multipartFiles) {
        return productService.createProductFilesByProductId(productId, multipartFiles);
    }

    @PutMapping("/update/{product-id}/files/{file-id}")
    public ResponseEntity<?> updateProductFiles(
            @PathVariable(value = "product-id") Long productId,
            @PathVariable(value = "file-id") Long fileId,
            @RequestParam("files")
            @NotNull(message = "Please provide files to upload")
            MultipartFile multipartFile
    ) {
        return productService.updateFilesByProductId(productId, fileId, multipartFile);
    }
}
