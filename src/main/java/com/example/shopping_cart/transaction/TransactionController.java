package com.example.shopping_cart.transaction;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    @PreAuthorize("hasAuthority('USER')")
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
            @RequestParam(value = "page-size")
            @NotNull(message = "Page size must not be null")
            @Min(value = 1) @Max(value = 20)
            Integer pageSize,
            @RequestParam(value = "page-number")
            @NotNull(message = "Page index must not be null")
            @Min(value = 1)
            Integer pageNumber
    ) {
        Page<TransactionResponseDTO> transactionResponseDTO =
                transactionService.findAllByAuthenticationAndPage(authentication, pageNumber, pageSize);

        return ResponseEntity.status(HttpStatus.OK).body(transactionResponseDTO);
    }
}
