package com.example.shopping_cart.order;

import com.example.shopping_cart.cart.ShoppingCart;
import com.example.shopping_cart.cart.ShoppingCartService;
import com.example.shopping_cart.product.Product;
import com.example.shopping_cart.product_quantity.ProductQuantity;
import com.example.shopping_cart.product_quantity.ProductQuantityService;
import com.example.shopping_cart.address.AddressMapper;
import com.example.shopping_cart.user.MyUser;
import com.example.shopping_cart.user.MyUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        Order order = OrderMapper.toOrderSave(orderRequestDTO);
        order.setUser(myUser);


        //Add Phone Number to User
        myUser.setPhoneNumber(orderRequestDTO.getPhoneNumber());
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

        //Add Order to User
        myUser.addOrder(order);

        OrderResponseDTO orderResponseDTO = OrderMapper.toOrderResponseDTO(savedOrder);
        orderResponseDTO.setMessage("Save order " + orderResponseDTO.getId() + " successfully.");
        return orderResponseDTO;
    }

    public Page<OrderResponseDTO> findAllThroughAuthentication (
            @NotNull Authentication authentication,
            @NotNull OrderRequestFindDTO orderRequestFindDTO
    ) {
        Pageable pageable = PageRequest.of(orderRequestFindDTO.getPageNumber(), orderRequestFindDTO.getPageSize());
        MyUser myUser = myUserService.findByUserAuthentication(authentication);
        List<Order> ordersOfMyUser = myUser.getOrders();
        if (ordersOfMyUser.isEmpty()) {
            throw new EntityNotFoundException("Order(s) not found");
        }
        List<OrderResponseDTO> orderResponseDTOList = ordersOfMyUser.stream()
                .map(OrderMapper::toOrderResponseDTO)
                .toList();
        return new PageImpl<>(
                orderResponseDTOList,
                pageable,
                orderResponseDTOList.size()
        );
    }

    public OrderResponseDTO findByIdAndAuthentication(
            @NotNull Authentication authentication,
            Long orderId) {
        MyUser myUser = myUserService.findByUserAuthentication(authentication);
        List<Order> ordersOfMyUser = myUser.getOrders();
        if (ordersOfMyUser.isEmpty()) {
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
            @NotNull Authentication authentication,
            Long orderId,
            @NotNull OrderUpdateDTO orderUpdateDTO
    ) {

        MyUser myUser = myUserService.findByUserAuthentication(authentication);
        List<Order> ordersOfMyUser = myUser.getOrders();
        if (ordersOfMyUser == null) {
            throw new EntityNotFoundException("Order(s) not found");
        }
        Order updateOrder = this.findById(orderId);
        if (!ordersOfMyUser.contains(updateOrder)) {
            throw new EntityNotFoundException("Order with id " + orderId + " not found.");
        }

        if (orderUpdateDTO.getOrderInfo() != null) {
            updateOrder.setOrderInfo(orderUpdateDTO.getOrderInfo());
        }
        if (orderUpdateDTO.getAnotherField() != null) {
            updateOrder.setAnotherField(orderUpdateDTO.getAnotherField());
        }
        if (orderUpdateDTO.getPhoneNumber() != null) {
            myUser.setPhoneNumber(orderUpdateDTO.getPhoneNumber());
        }
        if (orderUpdateDTO.getAddressRequestDTO().getHouseNumber() != null &&
                orderUpdateDTO.getAddressRequestDTO().getStreetName() != null &&
                orderUpdateDTO.getAddressRequestDTO().getWardName() != null &&
                orderUpdateDTO.getAddressRequestDTO().getCity() != null &&
                orderUpdateDTO.getAddressRequestDTO().getZipCode() != null
        ) {
            myUser.setAddress(AddressMapper.toAddress(orderUpdateDTO.getAddressRequestDTO()));
        }


        Order savedOrder = orderRepository.save(updateOrder);
        OrderResponseDTO orderResponseDTO = OrderMapper.toOrderResponseDTO(savedOrder);
        orderResponseDTO.setMessage("Update order " + orderResponseDTO.getId() + " successfully.");
        return orderResponseDTO;
    }
}