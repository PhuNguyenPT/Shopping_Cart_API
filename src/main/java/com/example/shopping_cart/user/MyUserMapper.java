package com.example.shopping_cart.user;

import com.example.shopping_cart.address.AddressMapper;
import com.example.shopping_cart.role.MyRole;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
}
