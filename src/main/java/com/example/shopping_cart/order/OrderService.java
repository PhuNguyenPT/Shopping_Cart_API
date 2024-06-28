package com.example.shopping_cart.order;

import com.example.shopping_cart.cart.ShoppingCart;
import com.example.shopping_cart.cart.ShoppingCartService;
import com.example.shopping_cart.product.Product;
import com.example.shopping_cart.product_quantity.ProductQuantity;
import com.example.shopping_cart.product_quantity.ProductQuantityService;
import com.example.shopping_cart.address.AddressMapper;
import com.example.shopping_cart.sort.SortDirectionMapper;
import com.example.shopping_cart.user.MyUser;
import com.example.shopping_cart.user.MyUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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
//            quantityService.deleteById(quantity.getId());
        }
        shoppingCartQuantities.clear();
        shoppingCart.setQuantities(shoppingCartQuantities);
        shoppingCart.setTotalAmount(BigDecimal.ZERO);
        ShoppingCart savedShoppingCart = shoppingCartService.save(shoppingCart);

        myUser.setShoppingCart(savedShoppingCart);

        Order savedOrder = orderRepository.save(order);

        //Add Order to User
        myUser.addOrder(order);

        OrderResponseDTO orderResponseDTO = OrderMapper.toOrderResponseDTO(savedOrder);
        orderResponseDTO.setMessage("Save order " + orderResponseDTO.getId() + " successfully.");
        return orderResponseDTO;
    }

    public Page<OrderResponseDTO> findAllThroughAuthenticationAndPageAndDirectionAndSortAttribute(
            @NotNull Authentication authentication,
            Integer pageNumber,
            Integer pageSize,
            String sortAttribute,
            String direction
    ) {
        MyUser myUser = myUserService.findByUserAuthentication(authentication);
        List<Order> ordersOfMyUser = myUser.getOrders();
        if (ordersOfMyUser.isEmpty()) {
            throw new EntityNotFoundException("Order(s) not found");
        }

        OrderSort sortEnumAttribute = OrderSortMapper.toOrderSortDefaultCreatedDate(sortAttribute);
        Sort.Direction sortDirection = SortDirectionMapper.toSortDirectionDefaultDesc(direction);

        List<Order> sortedOrder = sort(
                sortEnumAttribute,
                ordersOfMyUser,
                sortDirection
        );

        List<OrderResponseDTO> orderResponseDTOList = sortedOrder.stream()
                .map(OrderMapper::toOrderResponseDTO)
                .peek(orderResponseDTO -> orderResponseDTO.setMessage("Find order successfully"))
                .toList();

        Pageable pageable = PageRequest.of(
                pageNumber,
                pageSize
        );

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
    private static List<Order> sort(
            @NotNull OrderSort sortAttribute,
            List<Order> orders,
            Sort.Direction direction
    ) {
        List<Order> sortedOrder = new ArrayList<>();
        switch (sortAttribute) {
            case AMOUNT -> {
                sortedOrder = orders.stream()
                        .filter(order -> order.getTotalAmount() != null)
                        .sorted(Comparator.comparing(Order::getTotalAmount))
                        .collect(Collectors.toCollection(ArrayList::new));
            }
            case CREATED_DATE -> {
                sortedOrder = orders.stream()
                        .filter(order -> order.getCreatedDate() != null)
                        .sorted(Comparator.comparing(Order::getCreatedDate))
                        .collect(Collectors.toCollection(ArrayList::new));
            }
            case LAST_MODIFIED_DATE -> {
                sortedOrder = orders.stream()
                        .filter(order -> order.getLastModifiedDate() != null)
                        .sorted(Comparator.comparing(Order::getLastModifiedDate))
                        .collect(Collectors.toCollection(ArrayList::new));
            }
        }

        if (direction.isDescending()) {
            Collections.reverse(sortedOrder);
        }
        return sortedOrder;
    }

    @Transactional
    public OrderResponseDTODeliverer updateStatusOrder(
            Long orderId,
            @NotNull String status
    ) {
        Order order = this.findById(orderId);
        if (status.equals(Status.DELIVERING.getValue()) && order.getStatus().equals(Status.PAID.name())){
            order.setStatus(OrderStatusMapper.toOrderStatus(status).name());
        } else if (status.equals(Status.COMPLETE.getValue()) && order.getStatus().equals(Status.DELIVERING.name())){
            order.setStatus(OrderStatusMapper.toOrderStatus(status).name());
        } else {
            throw new IllegalArgumentException("Invalid status transition " + status);
        }
        Order updateOrder = orderRepository.save(order);
        OrderResponseDTODeliverer orderResponseDTODeliverer = OrderMapper.toOrderResponseDTODeliverer(updateOrder);
        orderResponseDTODeliverer.setMessage("Update status successfully");
        return orderResponseDTODeliverer;
    }

    public Page<OrderResponseDTODeliverer> findAllOrderForDeliverer(
            @NotNull
            Integer pageNumber,
            Integer pageSize,
            String sortAttribute,
            String direction
    ) {
        Sort sort = Sort.by(Sort.Order.desc(OrderSort.CREATED_DATE.getValue()));
        List<Order> ordersForDeliverer = orderRepository.findAll(sort);
        if (ordersForDeliverer.isEmpty()) {
            throw new EntityNotFoundException("Order(s) not found");
        }
        ordersForDeliverer = ordersForDeliverer.stream()
                .filter(order -> !order.getStatus().equals(Status.PROCESSING.name()))
                .collect(Collectors.toCollection(ArrayList::new));

        Sort.Direction sortDirection = SortDirectionMapper.toSortDirectionDefaultDesc(direction);
        if (sortDirection.isAscending()) {
            Collections.reverse(ordersForDeliverer);
        }

        List<OrderResponseDTODeliverer> orderResponseDTODelivererListList = ordersForDeliverer.stream()
                .map(OrderMapper::toOrderResponseDTODeliverer)
                .peek(orderResponseDTO -> orderResponseDTO.setMessage("Find order successfully"))
                .toList();

        Pageable pageable = PageRequest.of(
                pageNumber,
                pageSize
        );

        return new PageImpl<>(
                orderResponseDTODelivererListList,
                pageable,
                orderResponseDTODelivererListList.size()
        );
    }
    public Page<OrderResponseDTODeliverer> filterOrderByStatusForDeliverer(
            @NotNull
            Integer pageNumber,
            Integer pageSize,
            String statusAttribute,
            String direction
    ) {
        Sort sort = Sort.by(Sort.Order.desc(OrderSort.CREATED_DATE.getValue()));
        List<Order> ordersForDeliverer = orderRepository.findAll(sort);
        if (ordersForDeliverer.isEmpty()) {
            throw new EntityNotFoundException("Order(s) not found");
        }
        ordersForDeliverer = ordersForDeliverer.stream()
                .filter(order -> !order.getStatus().equals(Status.PROCESSING.name()))
                .collect(Collectors.toCollection(ArrayList::new));

        Sort.Direction sortDirection = SortDirectionMapper.toSortDirectionDefaultDesc(direction);
        if (sortDirection.isAscending()) {
            Collections.reverse(ordersForDeliverer);
        }
        if (statusAttribute.equalsIgnoreCase(Status.PAID.getValue())) {
            ordersForDeliverer = ordersForDeliverer.stream()
                    .filter(order -> order.getStatus().equals(Status.PAID.name()))
                    .collect(Collectors.toCollection(ArrayList::new));
        } else if (statusAttribute.equalsIgnoreCase(Status.DELIVERING.getValue())) {
            ordersForDeliverer = ordersForDeliverer.stream()
                    .filter(order -> order.getStatus().equals(Status.DELIVERING.name()))
                    .collect(Collectors.toCollection(ArrayList::new));
        } else if (statusAttribute.equalsIgnoreCase(Status.COMPLETE.getValue())) {
            ordersForDeliverer = ordersForDeliverer.stream()
                    .filter(order -> order.getStatus().equals(Status.COMPLETE.name()))
                    .collect(Collectors.toCollection(ArrayList::new));
        } else {
            ordersForDeliverer = ordersForDeliverer.stream()
                    .filter(order -> order.getStatus().equals(Status.PAID.name()))
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        List<OrderResponseDTODeliverer> orderResponseDTODelivererListList = ordersForDeliverer.stream()
                .map(OrderMapper::toOrderResponseDTODeliverer)
                .peek(orderResponseDTO -> orderResponseDTO.setMessage("Find order successfully"))
                .toList();

        Pageable pageable = PageRequest.of(
                pageNumber,
                pageSize
        );

        return new PageImpl<>(
                orderResponseDTODelivererListList,
                pageable,
                orderResponseDTODelivererListList.size()
        );
    }
}