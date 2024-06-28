package com.example.shopping_cart.user;

import com.example.shopping_cart.address.AddressMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class MyUserMapper {
    public static MyUserResponseDTO toMyUserResponseDTOFind(
            @NotNull MyUser myUser
    ) {
        return MyUserResponseDTO.builder()
                .message("Find user successfully")
                .id(myUser.getId())
                .dateOfBirth(myUser.getDateOfBirth())
                .email(myUser.getEmail())
                .phoneNumber(myUser.getPhoneNumber())
                .firstName(myUser.getFirstName())
                .lastName(myUser.getLastName())
                .addressResponseDTO(myUser.getAddress() != null ?
                        AddressMapper.toAddressResponseDTO(myUser.getAddress()) : null
                )
                .build();
    }
    public static MyUserResponseDTO toMyUserResponseDTO(
            @NotNull MyUser myUser
    ) {
        return MyUserResponseDTO.builder()
                .id(myUser.getId())
                .dateOfBirth(myUser.getDateOfBirth())
                .email(myUser.getEmail())
                .phoneNumber(myUser.getPhoneNumber())
                .firstName(myUser.getFirstName())
                .lastName(myUser.getLastName())
                .addressResponseDTO(myUser.getAddress() != null ?
                        AddressMapper.toAddressResponseDTO(myUser.getAddress()) : null
                )
                .build();
    }
}
