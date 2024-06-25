package com.example.shopping_cart.product_quantity;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductQuantityService {
    private final ProductQuantityRepository productQuantityRepository;

    public ProductQuantity findById(Long id) {
        return productQuantityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product Quantity with Id " + id + " not found"));
    }
    public void deleteById(Long id) {
        productQuantityRepository.deleteById(id);
    }
}
