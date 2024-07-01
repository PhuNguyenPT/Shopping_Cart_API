package com.example.shopping_cart.category;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @RequestParam(value = "page-size", defaultValue = "20")
            @Min(value = 1) @Max(value = 20)
            Integer pageSize,
            @RequestParam(value = "page-number", defaultValue = "1")
            @Min(value = 1)
            Integer pageNumber,
            @RequestParam(value = "id") List<Long> idList
            ) {

        CategoryResponseDTOFilter categoryResponseDTOFilter =
                categoryService.filterAllProductsByCategoryIdIn(
                        idList, pageNumber, pageSize
                );
        return ResponseEntity.status(HttpStatus.OK).body(categoryResponseDTOFilter);
    }
}
