package com.example.shopping_cart.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class MyUserController {

    private final MyUserService myUserService;

    @GetMapping("")
    private ResponseEntity<?> findAll() {
        return myUserService.findAll();
    }

    @GetMapping("/search/{user-id}")
    public ResponseEntity<?> findById(
            @PathVariable("user-id")UUID id
    ) {
        return myUserService.findById(id);
    }
}
