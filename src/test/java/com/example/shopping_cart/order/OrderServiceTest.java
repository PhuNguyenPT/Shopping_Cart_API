package com.example.shopping_cart.order;

import com.example.shopping_cart.address.Address;
import com.example.shopping_cart.address.AddressRequestDTO;
import com.example.shopping_cart.cart.ShoppingCart;
import com.example.shopping_cart.cart.ShoppingCartService;
import com.example.shopping_cart.product.Product;
import com.example.shopping_cart.product_quantity.ProductQuantity;
import com.example.shopping_cart.product_quantity.ProductQuantityService;
import com.example.shopping_cart.user.MyUser;
import com.example.shopping_cart.user.MyUserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MyUserService myUserService;

    @Mock
    private ShoppingCartService shoppingCartService;

    @Mock
    private ProductQuantityService quantityService;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveOrder() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        AddressRequestDTO addressRequestDTO = AddressRequestDTO.builder()
                .houseNumber("123")
                .streetName("Main St")
                .wardName("Ward 1")
                .city("City")
                .zipCode("12345")
                .build();
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO("orderInfo", "anotherField", BigInteger.valueOf(1234567890), addressRequestDTO);

        MyUser myUser = new MyUser();
        ShoppingCart shoppingCart = new ShoppingCart();

        // Create a new product and add it to the shopping cart
        Product product1 = new Product();
        product1.setId(1L);
        shoppingCart.getProducts().add(product1);

        // Create another product and add it to the shopping cart
        Product product2 = new Product();
        product2.setId(2L);
        shoppingCart.getProducts().add(product2);

        // Create product quantities and add them to the shopping cart
        ProductQuantity quantity1 = new ProductQuantity();
        quantity1.setProduct(product1);
        quantity1.setQuantity(1L);
        shoppingCart.getQuantities().add(quantity1);

        ProductQuantity quantity2 = new ProductQuantity();
        quantity2.setProduct(product2);
        quantity2.setQuantity(1L);
        shoppingCart.getQuantities().add(quantity2);

        myUser.setShoppingCart(shoppingCart);
        when(myUserService.findByUserAuthentication(authentication)).thenReturn(myUser);

        // Set up the OrderRepository mock to return a non-null Order with a non-null localDateTime
        Order order = new Order();
        order.setUser(myUser);
        order.setCreatedDate(LocalDateTime.now());
        order.setCreatedTimeZone(ZoneId.systemDefault());
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Act
        OrderResponseDTO result = orderService.saveOrder(authentication, orderRequestDTO);

        assertNotNull(result);
        assertEquals(order.getId(), result.getId());
        assertEquals(order.getUser().getName(), result.getName());
        assertEquals(order.getTotalAmount(), result.getTotalAmount());
        assertEquals(order.getDeliveryDate(), result.getDeliveryDate());
        assertEquals(order.getOrderInfo(), result.getOrderInfo());
        assertEquals(order.getAnotherField(), result.getAnotherField());
        assertEquals(order.getUser().getPhoneNumber(), result.getPhoneNumber());
        assertEquals(order.getUser().getAddress().getHouseNumber(), result.getAddressResponseDTO().getHouseNumber());
        assertEquals(order.getUser().getAddress().getStreetName(), result.getAddressResponseDTO().getStreetName());
        assertEquals(order.getUser().getAddress().getWardName(), result.getAddressResponseDTO().getWardName());
        assertEquals(order.getUser().getAddress().getCity(), result.getAddressResponseDTO().getCity());
        assertEquals(order.getUser().getAddress().getZipCode(), result.getAddressResponseDTO().getZipCode());
        assertEquals("Save order " + result.getId() + " successfully.", result.getMessage());
    }

    @Test
    void testSaveOrder_ShouldThrowEntityNotFoundException_WhenCartIsEmpty() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        AddressRequestDTO addressRequestDTO = AddressRequestDTO.builder()
                .houseNumber("123")
                .streetName("Main St")
                .wardName("Ward 1")
                .city("City")
                .zipCode("12345")
                .build();
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO("orderInfo", "anotherField", BigInteger.valueOf(1234567890), addressRequestDTO);

        MyUser myUser = new MyUser();
        ShoppingCart shoppingCart = new ShoppingCart();
        myUser.setShoppingCart(shoppingCart);
        when(myUserService.findByUserAuthentication(authentication)).thenReturn(myUser);

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> {
            orderService.saveOrder(authentication, orderRequestDTO);
        });
    }

    @Test
    void testFindAllThroughAuthenticationAndPageAndDirectionAndSortAttribute() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        int pageNumber = 0;
        int pageSize = 5;
        String sortAttribute = "AMOUNT";
        String direction = "DESC";

        MyUser myUser = new MyUser();
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Order order = new Order();
            order.setTotalAmount(BigDecimal.valueOf(i * 100));
            order.setUser(myUser); // Set the MyUser object to the Order

            // Create an Address and set it to the Order
            Address address = new Address();
            address.setHouseNumber("123");
            address.setStreetName("Main St");
            address.setWardName("Ward 1");
            address.setCity("City");
            address.setZipCode("12345");
            myUser.setAddress(address);

            order.setUser(myUser);
            order.setCreatedDate(LocalDateTime.now());
            order.setCreatedTimeZone(ZoneId.systemDefault());
            orders.add(order);
        }
        myUser.setOrders(orders);

        when(myUserService.findByUserAuthentication(authentication)).thenReturn(myUser);

        // Act
        Page<OrderResponseDTO> result = orderService.findAllThroughAuthenticationAndPageAndDirectionAndSortAttribute(authentication, pageNumber, pageSize, sortAttribute, direction);

        // Assert
        assertNotNull(result);
        assertEquals(pageSize, result.getPageable().getPageSize());
        assertTrue(result.getContent().get(0).getTotalAmount().compareTo(result.getContent().get(1).getTotalAmount()) > 0);
    }

    @Test
    void testFindByIdAndAuthentication() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        Long orderId = 1L;

        MyUser myUser = new MyUser();
        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        order.setId(orderId);
        order.setUser(myUser); // Set the user of the order
        order.setCreatedDate(LocalDateTime.now());
        order.setCreatedTimeZone(ZoneId.systemDefault());
        orders.add(order);
        myUser.setOrders(orders);

        // Create an Address and set it to the MyUser
        Address address = new Address();
        address.setHouseNumber("123");
        address.setStreetName("Main St");
        address.setWardName("Ward 1");
        address.setCity("City");
        address.setZipCode("12345");
        myUser.setAddress(address);

        when(myUserService.findByUserAuthentication(authentication)).thenReturn(myUser);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act
        OrderResponseDTO result = orderService.findByIdAndAuthentication(authentication, orderId);

        // Assert
        assertNotNull(result);
        assertEquals(orderId, result.getId());
        assertEquals("Search order " + orderId + " successfully.", result.getMessage());
    }

    @Test
    void testFindByIdAndAuthentication_ShouldThrowEntityNotFoundException_WhenOrderNotFound() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        Long orderId = 1L;

        MyUser myUser = new MyUser();
        List<Order> orders = new ArrayList<>();
        myUser.setOrders(orders);

        when(myUserService.findByUserAuthentication(authentication)).thenReturn(myUser);
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> {
            orderService.findByIdAndAuthentication(authentication, orderId);
        });
    }

    @Test
    void testDeleteBy() {
        // Arrange
        Long orderId = 1L;

        Order order = new Order();
        order.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        doNothing().when(orderRepository).deleteById(orderId);

        // Act
        String result = orderService.deleteBy(orderId);

        // Assert
        assertEquals("Order with id " + orderId + " is deleted successfully", result);
        verify(orderRepository, times(1)).deleteById(orderId);
    }

    @Test
    void testFindById() {
        // Arrange
        Long orderId = 1L;

        Order order = new Order();
        order.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act
        Order result = orderService.findById(orderId);

        // Assert
        assertNotNull(result);
        assertEquals(orderId, result.getId());
    }

    @Test
    void testFindById_ShouldThrowEntityNotFoundException_WhenOrderNotFound() {
        // Arrange
        Long orderId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> {
            orderService.findById(orderId);
        });
    }

    @Test
    void testUpdateOrderAttributes() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        Long orderId = 1L;
        AddressRequestDTO addressRequestDTO = AddressRequestDTO.builder()
                .houseNumber("123")
                .streetName("Main St")
                .wardName("Ward 1")
                .city("City")
                .zipCode("12345")
                .build();
        OrderUpdateDTO orderUpdateDTO = new OrderUpdateDTO("updatedOrderInfo", "updatedAnotherField", "updatedPhoneNumber", BigInteger.valueOf(9876543210L), addressRequestDTO);

        MyUser myUser = new MyUser();
        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        order.setId(orderId);
        order.setUser(myUser);
        order.setCreatedDate(LocalDateTime.now());
        order.setCreatedTimeZone(ZoneId.systemDefault());
        orders.add(order);
        myUser.setOrders(orders);

        when(myUserService.findByUserAuthentication(authentication)).thenReturn(myUser);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Act
        OrderResponseDTO result = orderService.updateOrderAttributes(authentication, orderId, orderUpdateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(orderId, result.getId());
        assertEquals(orderUpdateDTO.getOrderInfo(), result.getOrderInfo());
        assertEquals(orderUpdateDTO.getAnotherField(), result.getAnotherField());
        assertEquals(orderUpdateDTO.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(orderUpdateDTO.getAddressRequestDTO().getHouseNumber(), result.getAddressResponseDTO().getHouseNumber());
        assertEquals(orderUpdateDTO.getAddressRequestDTO().getStreetName(), result.getAddressResponseDTO().getStreetName());
        assertEquals(orderUpdateDTO.getAddressRequestDTO().getWardName(), result.getAddressResponseDTO().getWardName());
        assertEquals(orderUpdateDTO.getAddressRequestDTO().getCity(), result.getAddressResponseDTO().getCity());
        assertEquals(orderUpdateDTO.getAddressRequestDTO().getZipCode(), result.getAddressResponseDTO().getZipCode());
        assertEquals("Update order " + result.getId() + " successfully.", result.getMessage());
    }
}