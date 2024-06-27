package com.example.shopping_cart.product;

import lombok.Getter;

@Getter
public enum ProductSort {
    PRICE("price"),
    STOCK_QUANTITY("stockQuantity"),
    CREATED_DATE("createdDate"),
    LAST_MODIFIED_DATE("lastModifiedDate");

    private final String value;

    ProductSort(String value) {
        this.value = value;
    }
}
