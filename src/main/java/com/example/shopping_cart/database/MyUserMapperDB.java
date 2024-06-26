package com.example.shopping_cart.database;

import com.example.shopping_cart.role.MyRole;
import com.example.shopping_cart.user.MyUser;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
class MyUserMapperDB {
    static MyUser toMyUserADMIN(
            @NotNull PasswordEncoder passwordEncoder,
            List<MyRole> roles
    ) {
        return MyUser.builder()
                .email("admin@email.com")
                .firstName("first")
                .lastName("last")
                .password(passwordEncoder.encode("password"))
                .isEnabled(true)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .roles(roles)
                .createdBy("first" + " " + "last")
                .build();
    }

    static MyUser toMyUserDELIVERER(
            @NotNull PasswordEncoder passwordEncoder,
            MyRole role
    ) {
        return MyUser.builder()
                .email("deliverer@email.com")
                .firstName("first")
                .lastName("last")
                .password(passwordEncoder.encode("password"))
                .isEnabled(true)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .roles(Collections.singletonList(role))
                .createdBy("first" + " " + "last")
                .build();
    }
}
