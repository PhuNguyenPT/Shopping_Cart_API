package com.example.shopping_cart.transaction;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
public class TransactionResponseDTO {
    private String message;
    private final Long orderId;
    private final Long transactionId;
    private final BigDecimal amount;
    private final String transactionType;
    private final String currency;
    private final ZonedDateTime createdDate;
    private final ZonedDateTime lastModifiedDate;
    private final String createdBy;
    private final String lastModifiedBy;
}
