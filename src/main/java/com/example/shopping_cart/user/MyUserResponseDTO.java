package com.example.shopping_cart.user;

import com.example.shopping_cart.address.AddressResponseDTO;
import com.example.shopping_cart.order.Order;
import com.example.shopping_cart.order.OrderResponseDTO;
import com.example.shopping_cart.transaction.TransactionResponseDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class MyUserResponseDTO {
    private String message;
    private final UUID id;
    private final String firstName;
    private final String lastName;
    private final LocalDate dateOfBirth;
    private final BigInteger phoneNumber;
    private final String email;
    private final AddressResponseDTO addressResponseDTO;
}
