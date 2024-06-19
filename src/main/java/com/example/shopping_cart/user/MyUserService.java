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

    public MyUser findById(UUID id) {
        return myUserRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<MyUser> findAll() {
        return myUserRepository.findAll();
    }
}
