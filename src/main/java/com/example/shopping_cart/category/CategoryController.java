package com.example.shopping_cart.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping("/search")
    public ResponseEntity<?> findAll() {
        return categoryService.findAll();
    }

//    @GetMapping("/filter")
//    public ResponseEntity<?> filterAll(
//            @RequestBody List<CategoryRequestDTO> categoryRequestDTOList
//    ) {
//        return categoryService.filterAllProductsByCategoryIdIn(categoryRequestDTOList);
//    }
}
