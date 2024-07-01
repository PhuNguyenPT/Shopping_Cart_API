package com.example.shopping_cart.cart;

import com.example.shopping_cart.product.Product;
import com.example.shopping_cart.product.ProductService;
import com.example.shopping_cart.product_quantity.ProductQuantity;
import com.example.shopping_cart.product_quantity.ProductQuantityResponseDTO;
import com.example.shopping_cart.product_quantity.ProductQuantityService;
import com.example.shopping_cart.user.MyUser;
import com.example.shopping_cart.user.MyUserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ShoppingCartServiceTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private MyUserService myUserService;

    @Mock
    private ProductService productService;

    @Mock
    private ProductQuantityService productQuantityService;

    @InjectMocks
    private ShoppingCartService shoppingCartService;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave_NewCart() {
        UUID id = UUID.randomUUID();

        MyUser user = new MyUser();
        user.setId(id);

        when(myUserService.findByUserAuthentication(authentication)).thenReturn(user);

        ShoppingCart cart = new ShoppingCart();
        cart.setId(1L);
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(cart);
        user.setShoppingCart(null);

        cart.setUser(user);
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(cart);
        List<ShoppingCartRequestDTO> requestDTOs = new ArrayList<>();
        ShoppingCartResponseDTO responseDTO = shoppingCartService.save(authentication, requestDTOs);

        assertNotNull(responseDTO);
        verify(shoppingCartRepository, times(2)).save(any(ShoppingCart.class));
        verify(myUserService, times(1)).findByUserAuthentication(authentication);
    }

    @Test
    public void testSave_ExistingCart() {
        MyUser user = new MyUser();
        user.setId(UUID.randomUUID());
        ShoppingCart existingCart = new ShoppingCart();
        existingCart.setId(2L);
        existingCart.setUser(user);

        user.setShoppingCart(existingCart);
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(existingCart);
        when(myUserService.findByUserAuthentication(authentication)).thenReturn(user);

        List<ShoppingCartRequestDTO> requestDTOs = new ArrayList<>();
        ShoppingCartResponseDTO responseDTO = shoppingCartService.save(authentication, requestDTOs);

        assertNotNull(responseDTO);
        verify(shoppingCartRepository, times(1)).save(any(ShoppingCart.class));
        verify(myUserService, times(1)).findByUserAuthentication(authentication);
    }

    @Test
    public void testSave_DataIntegrityViolationException() {
        MyUser user = new MyUser();
        user.setShoppingCart(null);
        when(myUserService.findByUserAuthentication(authentication)).thenReturn(user);

        when(shoppingCartRepository.save(any(ShoppingCart.class)))
                .thenThrow(new DataIntegrityViolationException("Error"));

        List<ShoppingCartRequestDTO> requestDTOs = new ArrayList<>();
        assertThrows(DataIntegrityViolationException.class, () -> {
            shoppingCartService.save(authentication, requestDTOs);
        });
    }

    @Test
    public void testFindByPage() {
        // Arrange
        MyUser user = new MyUser();
        user.setId(UUID.randomUUID());
        ShoppingCart cart = new ShoppingCart();
        cart.setId(1L);
        user.setShoppingCart(cart);
        cart.setUser(user);
        when(myUserService.findByUserAuthentication(authentication)).thenReturn(user);

        ShoppingCartResponseDTOFind responseDTOFind = null;
        responseDTOFind = ShoppingCartResponseDTOFind.builder()
                .cartId(1L)
                .userId(user.getId())
                .totalAmount(BigDecimal.ZERO)
                .productQuantityResponseDTOPage(Page.empty())
                .build();

        // Assume that the shoppingCartService.findByPage method returns a non-empty page
//        when(shoppingCartService.findByPage(authentication, 0, 5)).thenReturn(responseDTOFind);

        // Act
        ShoppingCartResponseDTOFind responseDTOResult = shoppingCartService.findByPage(authentication, 0, 5);

        // Assert
        assertNotNull(responseDTOResult);
        assertEquals(responseDTOFind.getCartId(), responseDTOResult.getCartId());
        verify(myUserService, times(1)).findByUserAuthentication(authentication);
    }

    @Test
    public void testUpdateBy_CartNotFound() {
        MyUser user = new MyUser();
        user.setShoppingCart(null);
        when(myUserService.findByUserAuthentication(authentication)).thenReturn(user);

        List<ShoppingCartRequestDTO> requestDTOs = new ArrayList<>();
        assertThrows(EntityNotFoundException.class, () -> {
            shoppingCartService.updateBy(authentication, requestDTOs);
        });
    }

    @Test
    public void testUpdateBy_QuantitiesNotFound() {
        MyUser user = new MyUser();
        ShoppingCart cart = new ShoppingCart();
        user.setShoppingCart(cart);
        when(myUserService.findByUserAuthentication(authentication)).thenReturn(user);

        List<ShoppingCartRequestDTO> requestDTOs = new ArrayList<>();
        assertThrows(EntityNotFoundException.class, () -> {
            shoppingCartService.updateBy(authentication, requestDTOs);
        });
    }
//
//    @Test
//    public void testUpdateBy() {
//        // Arrange
//        MyUser user = new MyUser();
//        user.setId(UUID.randomUUID());
//        Product product = new Product();
//        product.setId(1L);
//        product.setPrice(1.0);
//        ProductQuantity productQuantity = new ProductQuantity();
//        productQuantity.setQuantity(1L);
//        List<ProductQuantity> quantities = new ArrayList<>();
//        quantities.add(productQuantity);
//        ShoppingCart cart = new ShoppingCart();
//        cart.setId(1L);
//        cart.setQuantities(quantities);
//        user.setShoppingCart(cart);
//        cart.setUser(user);
//        when(myUserService.findByUserAuthentication(authentication)).thenReturn(user);
//
//        List<ShoppingCartRequestDTO> requestDTOs = new ArrayList<>();
//        ShoppingCartResponseDTO expectedResponse = ShoppingCartResponseDTO.builder()
//                .cartId(1L)
//                .userId(user.getId())
//                .build();
//
//        when(shoppingCartService.updateBy(authentication, requestDTOs)).thenReturn(expectedResponse);
//
//        // Act
//        ShoppingCartResponseDTO actualResponse = shoppingCartService.updateBy(authentication, requestDTOs);
//
//        // Assert
//        assertNotNull(actualResponse);
//        assertEquals(expectedResponse.getCartId(), actualResponse.getCartId());
//        verify(myUserService, times(1)).findByUserAuthentication(authentication);
//        verify(shoppingCartService, times(1)).updateBy(authentication, requestDTOs);
//    }
}
