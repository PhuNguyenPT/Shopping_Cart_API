package com.example.shopping_cart.user;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class MyUserController {

    private final MyUserService myUserService;

    @NotNull
    @GetMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> findAll() {
        List<MyUser> myUsers = myUserService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(myUsers);
    }

    @GetMapping("/search/{user-id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> findById(
            @PathVariable("user-id")UUID id
    ) {
        MyUser myUser = myUserService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(myUser);
    }

    @GetMapping("/account")
    public ResponseEntity<?> findBy(
            Authentication authentication
    ) {
        MyUser myUser = myUserService.findByUserAuthentication(authentication);
        return ResponseEntity.status(HttpStatus.OK).body(myUser);
    }
}
