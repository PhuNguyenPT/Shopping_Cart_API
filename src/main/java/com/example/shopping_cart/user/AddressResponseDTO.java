package com.example.shopping_cart.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
