package com.example.shopping_cart.transaction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum TransactionSort {
    AMOUNT("amount"),
    CREATED_DATE("createdDate"),
    LAST_MODIFIED_DATE("lastModifiedDate");

    private final String value;

    TransactionSort(String value) {
        this.value = value;
    }
}
