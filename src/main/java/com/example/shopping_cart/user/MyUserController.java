package com.example.shopping_cart.user;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class MyUserController {

    private final MyUserService myUserService;

    @NotNull
    @PostMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> findAll(
            @RequestBody MyUserRequestDTO myUserRequestDTO
    ) {
        Page<MyUserResponseDTO> myUsersMyUserResponseDTOPage = myUserService.findAll(myUserRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(myUsersMyUserResponseDTOPage);
    }

    @GetMapping("/search/{user-id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> findById(
            @PathVariable("user-id")UUID id
    ) {
        MyUserResponseDTO myUserResponseDTO = myUserService.findUserAttributesById(id);
        return ResponseEntity.status(HttpStatus.OK).body(myUserResponseDTO);
    }

    @GetMapping("/account")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> findBy(
            Authentication authentication
    ) {
        MyUserResponseDTO myUserResponseDTO =
                myUserService.findUserAttributesByUserAuthentication(authentication);
        return ResponseEntity.status(HttpStatus.OK).body(myUserResponseDTO);
    }
}
