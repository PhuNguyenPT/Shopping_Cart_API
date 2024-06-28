package com.example.shopping_cart.order;

import com.example.shopping_cart.product_quantity.ProductQuantityMapper;
import com.example.shopping_cart.address.AddressMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;


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
                .phoneNumber(order.getUser().getPhoneNumber())
                .addressResponseDTO(AddressMapper.toAddressResponseDTO(order.getUser().getAddress()))
                .createdDate(
                        ZonedDateTime.of(
                                order.getCreatedDate(),
                                        order.getCreatedTimeZone()
                        )
                )
                .lastModifiedDate(
                        Optional.ofNullable(order.getLastModifiedDate())
                                .map(lastModifiedDate -> {
                                            if (order.getModifiedTimeZone() != null) {
                                                return ZonedDateTime.of(
                                                        lastModifiedDate,
                                                        ZoneId.of(String.valueOf(order.getModifiedTimeZone()))
                                                );
                                            }
                                            return null;
                                        }
                                )
                                .orElse(null)
                )
                .createdBy(order.getCreatedBy())
                .lastModifiedBy(order.getLastModifyBy())
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

    public static OrderResponseDTODeliverer toOrderResponseDTODeliverer(
            @NotNull Order order
    ) {
        return OrderResponseDTODeliverer.builder()
                .id(order.getId())
                .fullName(order.getUser().getFullName())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .deliveryDate(order.getDeliveryDate())
                .orderInfo(order.getOrderInfo())
                .phoneNumber(order.getUser().getPhoneNumber())
                .addressResponseDTO(AddressMapper.toAddressResponseDTO(order.getUser().getAddress()))
                .build();
    }
}
