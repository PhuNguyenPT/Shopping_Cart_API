package com.example.shopping_cart.address;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddressResponseDTO {
    private String houseNumber;
    private String streetName;
    private String wardName;
    private String city;
    private String zipCode;
}
