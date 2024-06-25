package com.example.shopping_cart.transaction;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TransactionRequestDTO {
    @NotNull(message = "Order id must not be null")
    @Min(value = 1, message = "Order id must be equal or greater than 1")
    private final Long orderId;
    @NotNull(message = "Transaction Type must not be null")
    @NotBlank(message = "Transaction Type must not be blank")
    private final String transactionType;
    @NotNull(message = "Currency must not be null")
    @NotBlank(message = "Currency must not be blank")
    private final String currency;
}
