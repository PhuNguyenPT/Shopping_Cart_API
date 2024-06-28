package com.example.shopping_cart.cart;

import com.example.shopping_cart.product.Product;
import com.example.shopping_cart.product.ProductService;
import com.example.shopping_cart.product_quantity.*;
import com.example.shopping_cart.user.MyUser;
import com.example.shopping_cart.user.MyUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {

    @Autowired
    private final ShoppingCartRepository shoppingCartRepository;
    private final MyUserService myUserService;
    private final ProductService productService;
    private final ProductQuantityService productQuantityService;

    @Transactional
    public ShoppingCartResponseDTO save(
            @NotNull Authentication authentication,
            @NotNull List<ShoppingCartRequestDTO> shoppingCartRequestDTOList
    ) {
        MyUser authenticatedUser = myUserService.findByUserAuthentication(authentication);

        ShoppingCart savedShoppingCart = authenticatedUser.getShoppingCart();

        if (savedShoppingCart == null) {
            try {
                ShoppingCart shoppingCart = ShoppingCart.builder()
                        .products(new ArrayList<>())
                        .quantities(new ArrayList<>())
                        .user(authenticatedUser)
                        .build();
                savedShoppingCart = shoppingCartRepository.save(shoppingCart);
                authenticatedUser.setShoppingCart(savedShoppingCart);
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
                productQuantityService.save(productQuantity);
            }
        });
        updatedShoppingCart.setCreatedBy(authenticatedUser.getFullName());
        ShoppingCart savedUpdatedShoppingCart = shoppingCartRepository.save(updatedShoppingCart);
        return ShoppingCartMapper.toShoppingCartResponseDTO(savedUpdatedShoppingCart);
    }

    @Transactional
    public ShoppingCart save(ShoppingCart shoppingCart) {
        return shoppingCartRepository.save(shoppingCart);
    }

    public ShoppingCartResponseDTOFind findByPage(
            @NotNull Authentication authentication,
            Integer pageNumber, Integer pageSize
    ) {
        MyUser authenticatedUser = myUserService.findByUserAuthentication(authentication);

        if (authenticatedUser.getShoppingCart() == null) {
            throw new EntityNotFoundException("User shopping cart not found");
        }
        ShoppingCart shoppingCart = authenticatedUser.getShoppingCart();

        if (shoppingCart.getQuantities() == null) {
            throw new EntityNotFoundException("Shopping Cart Quantities not found");
        }
        List<ProductQuantity> shoppingCartQuantities = shoppingCart.getQuantities();

        List<ProductQuantityResponseDTO> productQuantityResponseDTOList = shoppingCartQuantities.stream()
                .map(ProductQuantityMapper::toProductQuantityResponseDTO)
                .toList();

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<ProductQuantityResponseDTO> productQuantityResponseDTOPage = new PageImpl<>(
                productQuantityResponseDTOList,
                pageable,
                productQuantityResponseDTOList.size()
        );

        return ShoppingCartMapper.toShoppingCartResponseDTOFind(shoppingCart, productQuantityResponseDTOPage);
    }

    @Transactional
    public ShoppingCartResponseDTO updateBy(
            Authentication authentication,
            List<ShoppingCartRequestDTO> shoppingCartRequestDTOList
    ) {
        MyUser authenticatedUser = myUserService.findByUserAuthentication(authentication);
        ShoppingCart shoppingCart = authenticatedUser.getShoppingCart();
        if (shoppingCart == null) {
            throw new EntityNotFoundException("Shopping Cart not found");
        }

        List<ProductQuantity> shoppingCartQuantities = shoppingCart.getQuantities();

        if (shoppingCartQuantities == null || shoppingCartQuantities.isEmpty()) {
            throw new EntityNotFoundException("Shopping quantities not found");
        }

//        Map<Product, Long> productMap = shoppingCartQuantities.stream()
//                .collect(Collectors.toMap(ProductQuantity::getProduct, ProductQuantity::getQuantity));
        Map<Product, ProductQuantity> productQuantityMap = shoppingCartQuantities.stream()
                .collect(Collectors.toMap(ProductQuantity::getProduct, Function.identity()));

        for (ShoppingCartRequestDTO shoppingCartRequestDTO : shoppingCartRequestDTOList) {
            Product product = productService.findById(shoppingCartRequestDTO.getProductId());
//            Long quantity = productMap.get(product);
            ProductQuantity productQuantity = productQuantityMap.get(product);

            if (productQuantity == null) {
                productQuantity = ProductQuantityMapper.toProductQuantitySaveShoppingCart(
                        shoppingCart, shoppingCartRequestDTO.getQuantity(), product
                );
                shoppingCart.addQuantity(productQuantity);
            }

            productQuantity.setQuantity(shoppingCartRequestDTO.getQuantity());
            productQuantity.calculateTotalAmount(productQuantity.getProduct().getPrice());
            productQuantityService.save(productQuantity);
        }

        ShoppingCart savedShoppingCart = shoppingCartRepository.save(shoppingCart);

        return ShoppingCartMapper.toShoppingCartResponseDTO(savedShoppingCart);
    }
}
