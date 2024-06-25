package com.example.shopping_cart.transaction;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TransactionRequestDTOFind {
    private final String sortOrder;
    @NotNull(message = "Page size must not be null")
    @Min(value = 1) @Max(value = 20)
    Integer pageSize;
    @NotNull(message = "Page index must not be null")
    @Min(value = 1)
    Integer pageNumber;
}
