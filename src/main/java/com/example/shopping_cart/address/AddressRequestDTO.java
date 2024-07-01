package com.example.shopping_cart.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddressRequestDTO {
    @NotNull(message = "House Number must not be null")
    @NotBlank(message = "House Number is invalid")
    private String houseNumber;
    @NotNull(message = "Street Number must not be null")
    @NotBlank(message = "Street Number is invalid")
    private String streetName;
    @NotNull(message = "Ward Number must not be null")
    @NotBlank(message = "Ward Number is invalid")
    private String wardName;
    @NotNull(message = "City Number must not be null")
    @NotBlank(message = "City Number is invalid")
    private String city;
    @NotNull(message = "Zipcode must not be null")
    @NotBlank(message = "Zipcode Number is invalid")
    private String zipCode;
}
