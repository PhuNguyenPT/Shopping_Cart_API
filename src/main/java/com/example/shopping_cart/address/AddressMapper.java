package com.example.shopping_cart.address;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class AddressMapper {
    public static Address toAddress(@NotNull AddressRequestDTO addressRequestDTO) {
        return Address.builder()
                .houseNumber(addressRequestDTO.getHouseNumber())
                .streetName(addressRequestDTO.getStreetName())
                .wardName(addressRequestDTO.getWardName())
                .city(addressRequestDTO.getCity())
                .zipCode(addressRequestDTO.getZipCode())
                .build();
    }

    public static AddressResponseDTO toAddressResponseDTO(@NotNull Address address) {
        return AddressResponseDTO.builder()
                .houseNumber(address.getHouseNumber())
                .streetName(address.getStreetName())
                .wardName(address.getWardName())
                .city(address.getCity())
                .zipCode(address.getZipCode())
                .build();
    }
}
