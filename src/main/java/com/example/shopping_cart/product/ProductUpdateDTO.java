package com.example.shopping_cart.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ProductUpdateDTO {
    private final String name;
    private final Double price;
    private final Long stockQuantity;
    private final String description;
    private final List<Long> categoryIds;
}
