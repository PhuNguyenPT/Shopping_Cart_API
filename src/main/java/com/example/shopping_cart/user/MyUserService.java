package com.example.shopping_cart.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MyUserService {

    private final MyUserRepository myUserRepository;

    public ResponseEntity<?> findById(UUID id) {
        MyUser myUser = myUserRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        return ResponseEntity.status(HttpStatus.OK).body(myUser);
    }

    public ResponseEntity<?> findAll() {
        List<MyUser> myUsers = myUserRepository.findAll();
        if (myUsers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(myUsers);
    }
}
