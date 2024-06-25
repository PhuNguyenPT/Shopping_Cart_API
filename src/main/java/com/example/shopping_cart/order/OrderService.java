package com.example.shopping_cart.order;

import com.example.shopping_cart.cart.ShoppingCart;
import com.example.shopping_cart.cart.ShoppingCartService;
import com.example.shopping_cart.product.Product;
import com.example.shopping_cart.product_quantity.ProductQuantity;
import com.example.shopping_cart.product_quantity.ProductQuantityRepository;
import com.example.shopping_cart.product_quantity.ProductQuantityService;
import com.example.shopping_cart.user.AddressMapper;
import com.example.shopping_cart.user.MyUser;
import com.example.shopping_cart.user.MyUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MyUserService myUserService;
    private final ShoppingCartService shoppingCartService;
    private final ProductQuantityService quantityService;

    @Transactional
    public OrderResponseDTO saveOrder(
            @NotNull Authentication authentication,
            @NotNull OrderRequestDTO orderRequestDTO
    ) {
        MyUser myUser = myUserService.findByUserAuthentication(authentication);
        ShoppingCart shoppingCart = myUser.getShoppingCart();

        if (shoppingCart.getQuantities().isEmpty()) {
            throw new EntityNotFoundException("There is no Product in Your Cart");
        }
        Order order = OrderMapper.toOrder(orderRequestDTO);
        order.setUser(myUser);
        //Add Order to User
        myUser.addOrder(order);

        //Add Address to User
        myUser.setAddress(AddressMapper.toAddress(orderRequestDTO.getAddressRequestDTO()));

        List<Product> orderProducts = new ArrayList<>(shoppingCart.getProducts());
        order.setProducts(orderProducts);

        List<ProductQuantity> orderQuantities = new ArrayList<>(shoppingCart.getQuantities());
        //Remove CartId in ProductQuantities and Set OrderId to Quantities
        for (ProductQuantity quantity : orderQuantities) {
            quantity.setShoppingCart(null);
            quantity.setOrder(order);
        }
        order.setQuantities(orderQuantities);

        order.setTotalAmount(myUser.getShoppingCart().getTotalAmount());



        //Remove Everything in Cart
        List<ProductQuantity> shoppingCartQuantities = shoppingCart.getQuantities();
        for (ProductQuantity quantity : shoppingCartQuantities) {
            quantity.setShoppingCart(null);
            quantityService.deleteById(quantity.getId());
        }
        shoppingCartQuantities.clear();
        shoppingCart.setQuantities(shoppingCartQuantities);
        ShoppingCart savedShoppingCart = shoppingCartService.save(shoppingCart);

        myUser.setShoppingCart(savedShoppingCart);



        Order savedOrder = orderRepository.save(order);
        System.out.println(savedOrder.getQuantities());
        System.out.println(order.getQuantities());
        OrderResponseDTO orderResponseDTO = OrderMapper.toOrderResponseDTO(savedOrder);
        orderResponseDTO.setMessage("Save order " + orderResponseDTO.getId() + " successfully.");
        return orderResponseDTO;
    }

    public OrderResponseDTO findByIdAndAuthentication(
            @NotNull Authentication authentication,
            Long orderId) {
        MyUser myUser = myUserService.findByUserAuthentication(authentication);
        List<Order> ordersOfMyUser = myUser.getOrders();
        if (ordersOfMyUser == null) {
            throw new EntityNotFoundException("Order(s) not found");
        }
        Order foundOrder = this.findById(orderId);
        if (!ordersOfMyUser.contains(foundOrder)) {
            throw new EntityNotFoundException("Order with id " + orderId + " not found.");
        }
        OrderResponseDTO orderResponseDTO = OrderMapper.toOrderResponseDTO(foundOrder);
        orderResponseDTO.setMessage("Search order " + orderResponseDTO.getId() + " successfully.");
        return orderResponseDTO;
    }


    @Transactional
    public String deleteBy(Long orderId) {
        Order order = findById(orderId);
        orderRepository.deleteById(order.getId());
        return "Order with id " + orderId + " is deleted successfully";
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No order with " + id + " found"));
    }


    @Transactional
    public OrderResponseDTO updateOrderAttributes(
            Long id,
            @NotNull OrderUpdateDTO orderUpdateDTO
    ) {
        Order order = findById(id);
        if (orderUpdateDTO.getStatus() != null) {
            order.setStatus(orderUpdateDTO.getStatus());
        }
        if (orderUpdateDTO.getDeliveryDate() != null) {
            order.setDeliveryDate(orderUpdateDTO.getDeliveryDate());
        }
        if (orderUpdateDTO.getOrderInfo() != null) {
            order.setOrderInfo(orderUpdateDTO.getOrderInfo());
        }
        if (orderUpdateDTO.getAnotherField() != null) {
            order.setAnotherField(orderUpdateDTO.getAnotherField());
        }

        Order savedOrder = orderRepository.save(order);
        OrderResponseDTO orderResponseDTO = OrderMapper.toOrderResponseDTO(savedOrder);
        orderResponseDTO.setMessage("Update order " + orderResponseDTO.getId() + " successfully.");
        return orderResponseDTO;
    }
}