package com.example.shopping_cart.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
        ProductResponseDTO productResponseDTO = productService.save(productRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDTO);
    }

    @GetMapping("/search/{product-name}")
    public ResponseEntity<?> searchBy(
            @PathVariable(value = "product-name", required = false)
            String productName
    ) {
        List<ProductResponseDTO> productResponseDTO = productService.findBy(productName);
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchByProductNameAndPage(
            @RequestParam(value = "product-name", required = false) String productName,
            @RequestParam(value = "page-size") @NotNull(message = "Page size must not be null")
            Integer pageSize,
            @RequestParam(value = "page-number") @NotNull(message = "Page number must not be null")
            Integer pageNumber
    ) {
        Page<ProductResponseDTO> productResponseDTOPage = productService.findByProductNameAndPage(productName, pageNumber, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDTOPage);
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
        ProductResponseDTO productResponseDTO = productService.createProductFilesByProductId(productId, multipartFiles);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponseDTO);
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
        ProductResponseDTO productResponseDTO = productService.updateFileByProductIdAndFileId(productId, fileId, multipartFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponseDTO);
    }

    @PatchMapping("/update/{product-id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateProductAttributes(
            @PathVariable(value = "product-id") Long id,
            @ModelAttribute @Valid ProductUpdateDTO productUpdateDTO
    ) {
        ProductResponseDTO productResponseDTO = productService.updateProductAttributes(id, productUpdateDTO);
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDTO);
    }
}
