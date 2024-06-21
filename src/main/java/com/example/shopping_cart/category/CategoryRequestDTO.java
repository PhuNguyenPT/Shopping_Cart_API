package com.example.shopping_cart.category;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryRequestDTO {
    @NotNull(message = "Product id must not be null")
    @Min(value = 1, message = "Product id must be greater than 0")
    private final Long categoryId;
    private final String name;
}
