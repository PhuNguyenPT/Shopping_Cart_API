package com.example.shopping_cart.order;

import lombok.Getter;

@Getter
public enum Status {
    PROCESSING("processing"),
    PAID("paid"),
    DELIVERING("delivering"),
    COMPLETE("complete"),
    NONE("none");

    private final String value;

    Status(String value) { this.value = value; }
}
