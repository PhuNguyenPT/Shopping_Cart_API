package com.example.shopping_cart.category;

import com.example.shopping_cart.product.ProductResponseDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.Map;

@Getter
@Setter
@Builder
public class CategoryResponseDTOFilter {
    private final Map<Long, String> namesMap;
    private final Page<ProductResponseDTO> productsPage;
}
