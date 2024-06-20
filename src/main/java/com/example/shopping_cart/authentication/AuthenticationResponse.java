package com.example.shopping_cart.authentication;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class AuthenticationResponse {
    private String token;
}
