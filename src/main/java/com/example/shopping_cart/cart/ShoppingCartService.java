package com.example.shopping_cart.cart;

import com.example.shopping_cart.product.Product;
import com.example.shopping_cart.product.ProductService;
import com.example.shopping_cart.product_quantity.ProductQuantity;
import com.example.shopping_cart.product_quantity.ProductQuantityRepository;
import com.example.shopping_cart.user.MyUser;
import com.example.shopping_cart.user.MyUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {

    @Autowired
    private final ShoppingCartRepository shoppingCartRepository;
    private final MyUserService myUserService;
    private final ProductService productService;
    private final ProductQuantityRepository productQuantityRepository;

    @Transactional
    public ShoppingCartResponseDTO save(
            @NotNull Authentication authentication,
            @NotNull List<ShoppingCartRequestDTO> shoppingCartRequestDTOList
    ) {
        MyUser myUser = myUserService.findByUserAuthentication(authentication);

        ShoppingCart savedShoppingCart = myUser.getShoppingCart();

        if (savedShoppingCart == null) {
            try {
                ShoppingCart shoppingCart = ShoppingCart.builder()
                        .products(new ArrayList<>())
                        .quantities(new ArrayList<>())
                        .user(myUser)
                        .build();
                savedShoppingCart = shoppingCartRepository.save(shoppingCart);
                myUser.setShoppingCart(savedShoppingCart);
            } catch (Exception ex) {
                throw new DataIntegrityViolationException(ex.getMessage());
            }
        }

        List<Product> products = shoppingCartRequestDTOList.stream()
                .map(ShoppingCartRequestDTO::getProductId)
                .distinct()
                .map(productService::findById)
                .toList();

        ShoppingCart updatedShoppingCart = ShoppingCartMapper.toShoppingCart(
                products, shoppingCartRequestDTOList, savedShoppingCart
        );

        updatedShoppingCart.getQuantities().forEach(productQuantity ->
                productQuantity.calculateTotalAmount(productQuantity.getProduct().getPrice())
        );

        BigDecimal cartTotalAmount = updatedShoppingCart.getQuantities().stream()
                .map(ProductQuantity::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        updatedShoppingCart.setTotalAmount(cartTotalAmount);

        updatedShoppingCart.getQuantities().forEach(productQuantity -> {
            if (productQuantity.getId() == null) {
                productQuantityRepository.save(productQuantity);
            }
        });
        updatedShoppingCart.setCreatedBy(myUser.getFullName());
        ShoppingCart savedUpdatedShoppingCart = shoppingCartRepository.save(updatedShoppingCart);
        return ShoppingCartMapper.toShoppingCartResponseDTO(savedUpdatedShoppingCart);
    }

    public ShoppingCart save(ShoppingCart shoppingCart) {
        return shoppingCartRepository.save(shoppingCart);
    }

    public ShoppingCartResponseDTO findBy(
            @NotNull Authentication authentication
    ) {
        MyUser authenticatedUser = myUserService.findByUserAuthentication(authentication);

        ShoppingCart shoppingCart = authenticatedUser.getShoppingCart();
        if (shoppingCart == null) {
            throw new EntityNotFoundException("User shopping cart not found");
        }
        return ShoppingCartMapper.toShoppingCartResponseDTOFind(shoppingCart);
    }
}
