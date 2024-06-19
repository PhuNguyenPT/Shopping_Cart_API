package com.example.shopping_cart.cart;

import com.example.shopping_cart.product.Product;
import com.example.shopping_cart.product.ProductService;
import com.example.shopping_cart.product_quantity.ProductQuantity;
import com.example.shopping_cart.product_quantity.ProductQuantityMapper;
import com.example.shopping_cart.product_quantity.ProductQuantityRepository;
import com.example.shopping_cart.product_quantity.ProductQuantityResponseDTO;
import com.example.shopping_cart.user.MyUser;
import com.example.shopping_cart.user.MyUserService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final MyUserService myUserService;
    private final ProductService productService;
    private final ProductQuantityRepository productQuantityRepository;

    @Transactional
    public ShoppingCartResponseDTO save(
            UUID userId,
            @NotNull List<ShoppingCartRequestDTO> shoppingCartRequestDTOList
    ) {
        MyUser myUser = myUserService.findById(userId);

        ShoppingCart savedShoppingCart = myUser.getShoppingCart();

        if (savedShoppingCart == null) {
            ShoppingCart shoppingCart = ShoppingCart.builder()
                    .products(new ArrayList<>())
                    .quantities(new ArrayList<>())
                    .user(myUser)
                    .build();
            savedShoppingCart = shoppingCartRepository.save(shoppingCart);
            myUser.setShoppingCart(savedShoppingCart);
        }

        List<Product> products = shoppingCartRequestDTOList.stream()
                .map(ShoppingCartRequestDTO::getProductId)
                .distinct()
                .map(productService::findById)
                .toList();

        ShoppingCart updatedShoppingCart = ShoppingCartMapper.toShoppingCart(
                myUser, products, shoppingCartRequestDTOList, savedShoppingCart
        );

        updatedShoppingCart.getQuantities().forEach(productQuantity -> {
            productQuantity.calculateTotalAmount(productQuantity.getProduct().getPrice());
        });

        BigDecimal cartTotalAmount = updatedShoppingCart.getQuantities().stream()
                .map(ProductQuantity::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        updatedShoppingCart.setTotalAmount(cartTotalAmount);

        updatedShoppingCart.getQuantities().forEach(productQuantity -> {
            if (productQuantity.getId() == null) {
                productQuantityRepository.save(productQuantity);
            }
        });
        ShoppingCart savedUpdatedShoppingCart = shoppingCartRepository.save(updatedShoppingCart);
        ShoppingCartResponseDTO shoppingCartResponseDTO = ShoppingCartMapper.toShoppingCartResponseDTO(savedUpdatedShoppingCart);
        return shoppingCartResponseDTO;
    }
}
