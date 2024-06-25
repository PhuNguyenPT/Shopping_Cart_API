package com.example.shopping_cart.address;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@Builder
public class Address {
    private String houseNumber;
    private String streetName;
    private String wardName;
    private String city;
    private String zipCode;
}
