package com.example.shopping_cart.email;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmailTemplate {
    ACTIVATE_ACCOUNT("activate_account");

    private final String value;
}
