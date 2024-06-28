package com.example.shopping_cart.user;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> findAll(
            @RequestParam(value = "page-size", defaultValue = "20")
            @Min(value = 1) @Max(value = 20)
            Integer pageSize,
            @RequestParam(value = "page-number", defaultValue = "1")
            @Min(value = 1)
            Integer pageNumber
    ) {
        Page<MyUserResponseDTO> myUsersMyUserResponseDTOPage =
                myUserService.findAll(pageNumber, pageSize);
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

    @PatchMapping("/update")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> updateUserAttributes(
            Authentication authentication,
            @RequestBody MyUserRequestDTO myUserRequestDTO
    ) {
        MyUserResponseDTO myUserResponseDTO =
                myUserService.updateUserAttributesByAuthentication(
                        authentication, myUserRequestDTO
                );
        return ResponseEntity.status(HttpStatus.OK).body(myUserResponseDTO);
    }
}
