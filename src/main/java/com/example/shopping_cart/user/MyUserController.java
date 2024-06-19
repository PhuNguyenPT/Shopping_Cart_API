package com.example.shopping_cart.user;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private ResponseEntity<?> findAll() {
        List<MyUser> myUsers = myUserService.findAll();
        if (myUsers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(myUsers);
    }

    @GetMapping("/search/{user-id}")
    public ResponseEntity<?> findById(
            @PathVariable("user-id")UUID id
    ) {
        MyUser myUser = myUserService.findById(id);
        if (myUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(myUser);
    }
}
