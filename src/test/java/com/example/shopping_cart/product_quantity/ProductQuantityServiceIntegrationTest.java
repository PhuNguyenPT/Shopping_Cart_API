//package com.example.shopping_cart.product_quantity;
//
//import com.example.shopping_cart.cart.ShoppingCart;
//import com.example.shopping_cart.product.Product;
//import jakarta.persistence.EntityNotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//public class ProductQuantityServiceIntegrationTest {
//
//    @Autowired
//    private ProductQuantityService productQuantityService;
//
//    @Autowired
//    private ProductQuantityRepository productQuantityRepository;
//
//    private ProductQuantity testProductQuantity;
//
//    @BeforeEach
//    void setUp() {
//        Product product = new Product();
//        product.setId(1L);
//
//        ShoppingCart shoppingCart = new ShoppingCart();
//        shoppingCart.setId(1L);
//
//        testProductQuantity = ProductQuantity.builder()
//                .product(product)
//                .shoppingCart(shoppingCart)
//                .quantity(2L)
//                .totalAmount(new BigDecimal("20.00"))
//                .build();
//
//        testProductQuantity = productQuantityRepository.save(testProductQuantity);
//    }
//
//    @Test
//    void testFindById() {
//        ProductQuantity found = productQuantityService.findById(testProductQuantity.getId());
//        assertNotNull(found);
//        assertEquals(testProductQuantity.getId(), found.getId());
//    }
//
//    @Test
//    void testFindByIdNotFound() {
//        assertThrows(EntityNotFoundException.class, () -> productQuantityService.findById(999L));
//    }
//
//    @Test
//    void testSave() {
//        ProductQuantity newProductQuantity = ProductQuantity.builder()
//                .product(new Product())
//                .shoppingCart(new ShoppingCart())
//                .quantity(3L)
//                .totalAmount(new BigDecimal("30.00"))
//                .build();
//
//        ProductQuantity saved = productQuantityService.save(newProductQuantity);
//        assertNotNull(saved.getId());
//        assertEquals(newProductQuantity.getQuantity(), saved.getQuantity());
//        assertEquals(newProductQuantity.getTotalAmount(), saved.getTotalAmount());
//    }
//
//    @Test
//    void testDeleteById() {
//        productQuantityService.deleteById(testProductQuantity.getId());
//        assertFalse(productQuantityRepository.existsById(testProductQuantity.getId()));
//    }
//}