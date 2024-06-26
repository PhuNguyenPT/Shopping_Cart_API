package com.example.shopping_cart.database;

import com.example.shopping_cart.role.MyRole;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
class MyRoleMapperDB {
    static MyRole toMyRole(@NotNull MyRole.Value authority) {
        return switch (authority) {
            case USER -> MyRole.builder()
                    .authority(
                            MyRole.Value.USER.name()
                    )
                    .build();
            case ADMIN -> MyRole.builder()
                    .authority(
                            MyRole.Value.ADMIN.name()
                    )
                    .build();
            case DELIVERER -> MyRole.builder()
                    .authority(
                            MyRole.Value.DELIVERER.name()
                    )
                    .build();
        };
    }
}
