package com.example.shopping_cart.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> save(
            Authentication authentication,
            @RequestBody TransactionRequestDTO transactionRequestDTO
    ) {
        TransactionResponseDTO transactionResponseDTO =
                transactionService.saveByAuthentication(
                        authentication,
                        transactionRequestDTO
                );
        return ResponseEntity.status(HttpStatus.OK).body(transactionResponseDTO);
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> findAll(
            Authentication authentication,
            @RequestBody TransactionRequestDTOFind transactionRequestDTOFind
    ) {
        Page<TransactionResponseDTO> transactionResponseDTO =
                transactionService.findAllByAuthenticationAndPage(
                        authentication, transactionRequestDTOFind
                );
        return ResponseEntity.status(HttpStatus.OK).body(transactionResponseDTO);
    }
}
