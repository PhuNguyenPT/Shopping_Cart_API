package com.example.shopping_cart.order;

import lombok.Getter;

@Getter
public enum OrderSort {
    AMOUNT("amount"),
    CREATED_DATE("createdDate"),
    LAST_MODIFIED_DATE("lastModifiedDate"),
    STATUS("status");

    private final String value;

    OrderSort(String value) { this.value = value; }
}
