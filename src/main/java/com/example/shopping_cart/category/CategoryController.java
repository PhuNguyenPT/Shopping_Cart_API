package com.example.shopping_cart.category;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping("")
    public ResponseEntity<?> findAll() {
        return categoryService.findAll();
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterAllBy(
            @RequestBody @Valid
            CategoryRequestFilterDTO categoryRequestFilterDTO
    ) {

        CategoryResponseDTOFilter categoryResponseDTOFilter =
                categoryService.filterAllProductsByCategoryIdIn(
                        categoryRequestFilterDTO
                );
        return ResponseEntity.status(HttpStatus.OK).body(categoryResponseDTOFilter);
    }
}
