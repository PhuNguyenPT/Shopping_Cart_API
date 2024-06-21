package com.example.shopping_cart.order;

import com.example.shopping_cart.product.Product;
import com.example.shopping_cart.product_quantity.ProductQuantity;
import com.example.shopping_cart.user.MyUser;
import com.example.shopping_cart.user.MyUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Transactional
    public OrderResponseDTO saveOrder(
            @NotNull Authentication authentication,
            @NotNull OrderRequestDTO orderRequestDTO
            ) {
        try {
            MyUser myUser = myUserService.findByUserAuthentication(authentication);

            Order order = OrderMapper.toOrder(orderRequestDTO);
            order.setUser(myUser);

            List<Product> orderProducts = new ArrayList<>(myUser.getShoppingCart().getProducts());
            order.setProducts(orderProducts);

            List<ProductQuantity> orderQuantities = new ArrayList<>(myUser.getShoppingCart().getQuantities());
            order.setQuantities(orderQuantities);

            order.setTotalAmount(myUser.getShoppingCart().getTotalAmount());

            Order savedOrder = orderRepository.save(order);
            System.out.println(savedOrder.getQuantities());
            System.out.println(order.getQuantities());
            OrderResponseDTO orderResponseDTO = OrderMapper.toOrderResponseDTO(savedOrder);
            orderResponseDTO.setMessage("Save order " + orderResponseDTO.getId() + " successfully.");
            return orderResponseDTO;

        } catch (RuntimeException e) {
            throw new RuntimeException("Cannot save order");
        }
    }

    public OrderResponseDTO searchOrderById(
            @NotNull Authentication authentication,
            Long orderId) {
        MyUser myUser = myUserService.findByUserAuthentication(authentication);
        List<Order> ordersOfMyUser = new ArrayList<>(myUser.getOrders());
        for (Order order: ordersOfMyUser) {
            if (order.getId().equals(orderId)) {
                System.out.println(order.getQuantities());
                OrderResponseDTO orderResponseDTO = OrderMapper.toOrderResponseDTO(order);
                orderResponseDTO.setMessage("Search order" + orderResponseDTO.getId() + " successfully.");
                return orderResponseDTO;
            }
        }
        return null;
    }


    @Transactional
    public String deleteBy(Long orderId) {
        Order order = findById(orderId);
        orderRepository.deleteById(order.getId());
        String responseMessage = "Order with id " + orderId + " is deleted successfully";
        return responseMessage;
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No order with " + id + "found"));
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