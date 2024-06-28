package com.example.shopping_cart.user;

import com.example.shopping_cart.address.AddressRequestDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class MyUserRequestDTO {
    private final String firstName;
    private final String lastName;
    private final LocalDate dateOfBirth;
    private final BigInteger phoneNumber;
    private final String email;
    private final AddressRequestDTO addressRequestDTO;
}
