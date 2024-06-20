package com.example.shopping_cart.category;

import com.example.shopping_cart.product.Product;
import com.example.shopping_cart.product.ProductResponseDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CategoryResponseDTO {
    private final Long id;
    private final String name;
    private final List<ProductResponseDTO> productResponseDTOList;
}
