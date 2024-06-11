package com.example.shopping_cart.user;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {
    private String houseNumber;
    private String streetName;
    private String wardName;
    private String city;
    private String zipCode;
}
