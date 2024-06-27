package com.example.shopping_cart.order;

import com.example.shopping_cart.product_quantity.ProductQuantityResponseDTO;
import com.example.shopping_cart.address.AddressResponseDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderResponseDTO {
    private String message;
    private final Long id;
    private final String name;
    private final BigDecimal totalAmount;
    private final String status;
    private final LocalDateTime deliveryDate;
    private final String orderInfo;
    private final String anotherField;
    private final ZonedDateTime createdDate;
    private final ZonedDateTime lastModifiedDate;
    private final String createdBy;
    private final String lastModifiedBy;

    private final List<ProductQuantityResponseDTO> productQuantityResponseDTOList;

    private final BigInteger phoneNumber;
    private final AddressResponseDTO addressResponseDTO;
}
