package com.example.shopping_cart.product_quantity;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductQuantityService {
    private final ProductQuantityRepository productQuantityRepository;

    public ProductQuantity findById(Long id) {
        return productQuantityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product Quantity with Id " + id + " not found"));
    }

    @Transactional
    public void deleteById(Long id) {
        productQuantityRepository.deleteById(id);
    }

    @Transactional
    public ProductQuantity save(ProductQuantity productQuantity) {
        return productQuantityRepository.save(productQuantity);
    }
}
