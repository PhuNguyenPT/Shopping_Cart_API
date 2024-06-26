package com.example.shopping_cart.database;

import com.example.shopping_cart.role.MyRole;
import com.example.shopping_cart.role.MyRoleService;
import com.example.shopping_cart.user.MyUser;
import com.example.shopping_cart.user.MyUserService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
class DatabaseUtil {
    private final MyRoleService myRoleService;
    private final MyUserService myUserService;
    private final PasswordEncoder passwordEncoder;
    @Bean
    public CommandLineRunner commandLineRunner(
    ) {
        return args -> {
            if (myRoleService.findByAuthorityOrElseReturnNull(MyRole.Value.USER.name()) == null) {
                myRoleService.save(MyRoleMapperDB.toMyRole(MyRole.Value.USER));
            }
            if (myRoleService.findByAuthorityOrElseReturnNull(MyRole.Value.ADMIN.name()) == null) {
                myRoleService.save(MyRoleMapperDB.toMyRole(MyRole.Value.ADMIN));
            }
            if (myRoleService.findByAuthorityOrElseReturnNull(MyRole.Value.DELIVERER.name()) == null) {
                myRoleService.save(MyRoleMapperDB.toMyRole(MyRole.Value.DELIVERER));
            }

            MyRole roleAdmin = myRoleService.findByAuthority(MyRole.Value.ADMIN);
            MyRole roleUser = myRoleService.findByAuthority(MyRole.Value.USER);
            MyRole roleDeliverer = myRoleService.findByAuthority(MyRole.Value.DELIVERER);
            List<MyRole> roles = Arrays.asList(roleAdmin, roleUser, roleDeliverer);
            MyUser admin = MyUserMapperDB.toMyUserADMIN(passwordEncoder, Arrays.asList(roleAdmin, roleUser));
            if (myUserService.findByEmailOrElseReturnNull(admin.getEmail()) == null) {
                myUserService.save(admin);
            } else {
                admin = myUserService.findByEmail(admin.getEmail());
                updateUser(admin, roles);
            }

            MyUser deliverer = MyUserMapperDB.toMyUserDELIVERER(passwordEncoder, roleDeliverer);
            if (myUserService.findByEmailOrElseReturnNull(deliverer.getEmail()) == null) {
                myUserService.save(deliverer);
            } else {
                deliverer = myUserService.findByEmail(deliverer.getEmail());
                updateUser(deliverer, roles);

            }
        };
    }
    private void updateUser(@NotNull MyUser myUser, @NotNull List<MyRole> roles) {
        MyRole adminRole = roles.stream()
                .filter(myRole -> myRole.getAuthority().equals(MyRole.Value.ADMIN.name()))
                .findFirst()
                .orElse(null);
        MyRole userRole = roles.stream()
                .filter(myRole -> myRole.getAuthority().equals(MyRole.Value.USER.name()))
                .findFirst()
                .orElse(null);
        MyRole delivererRole = roles.stream()
                .filter(myRole -> myRole.getAuthority().equals(MyRole.Value.DELIVERER.name()))
                .findFirst()
                .orElse(null);
        MyUser foundUser = myUserService.findByEmail(myUser.getEmail());
        if (foundUser.getRoles().contains(adminRole) && foundUser.getRoles().contains(userRole)) {
            foundUser.setEmail("admin@email.com");
            foundUser.setFirstName("first");
            foundUser.setLastName("last");
            foundUser.setPassword(passwordEncoder.encode("password"));
            foundUser.setAccountNonExpired(true);
            foundUser.setAccountNonLocked(true);
            foundUser.setCredentialsNonExpired(true);
            foundUser.setEnabled(true);
            foundUser.setRoles(Arrays.asList(adminRole, userRole));
            foundUser.setCreatedBy(foundUser.getFullName());
            foundUser.setLastModifyBy(foundUser.getFullName());
            myUserService.save(foundUser);
        }
        if (foundUser.getRoles().contains(delivererRole)) {
            foundUser.setEmail("deliverer@email.com");
            foundUser.setFirstName("first");
            foundUser.setLastName("last");
            foundUser.setPassword(passwordEncoder.encode("password"));
            foundUser.setAccountNonExpired(true);
            foundUser.setAccountNonLocked(true);
            foundUser.setCredentialsNonExpired(true);
            foundUser.setEnabled(true);
            foundUser.setRoles(Collections.singletonList(delivererRole));
            foundUser.setCreatedBy(foundUser.getFullName());
            foundUser.setLastModifyBy(foundUser.getFullName());
            myUserService.save(foundUser);
        }
    }
}
