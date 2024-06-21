package com.example.shopping_cart.order;

import com.example.shopping_cart.product_quantity.ProductQuantity;
import com.example.shopping_cart.product_quantity.ProductQuantityMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class OrderMapper {
    public static OrderResponseDTO toOrderResponseDTO(@NotNull Order order) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .name(order.getUser().getName())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .deliveryDate(order.getDeliveryDate())
                .orderInfo(order.getOrderInfo())
                .anotherField(order.getAnotherField())
                .productQuantityResponseDTOList(
                        order.getQuantities().stream()
                                .map(ProductQuantityMapper::toProductQuantityResponseDTOSave)
                                .toList()
                )
                .build();
    }

    public static Order toOrder(@NotNull OrderRequestDTO orderRequestDTO) {
        return Order.builder()
                .status(orderRequestDTO.getStatus())
                .deliveryDate(LocalDateTime.now())
                .orderInfo(orderRequestDTO.getOrderInfo())
                .anotherField(orderRequestDTO.getAnotherField())
                .build();
    }
}
