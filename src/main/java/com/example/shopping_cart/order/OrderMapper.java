package com.example.shopping_cart.order;

import com.example.shopping_cart.product_quantity.ProductQuantityMapper;
import com.example.shopping_cart.user.AddressMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class OrderMapper {
    public static OrderResponseDTO toOrderResponseDTO(
            @NotNull Order order
    ) {
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
                                .map(ProductQuantityMapper::toProductQuantityResponseDTOSaveOrder)
                                .toList()
                )
                .addressResponseDTO(AddressMapper.toAddressResponseDTO(order.getUser().getAddress()))
                .build();
    }

    public static Order toOrderSave(@NotNull OrderRequestDTO orderRequestDTO) {
        return Order.builder()
                .status(Status.PROCESSING.name())
                .deliveryDate(LocalDateTime.now())
                .orderInfo(orderRequestDTO.getOrderInfo())
                .anotherField(orderRequestDTO.getAnotherField())
                .build();
    }
}
