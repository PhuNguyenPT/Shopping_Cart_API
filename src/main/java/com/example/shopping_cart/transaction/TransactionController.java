package com.example.shopping_cart.transaction;

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
            @RequestParam(value = "page-size", defaultValue = "20")
            @Min(value = 1) @Max(value = 20)
            Integer pageSize,
            @RequestParam(value = "page-number", defaultValue = "1")
            @Min(value = 1)
            Integer pageNumber,
            @RequestParam(value = "sort", required = false, defaultValue = "createdDate")
            String sortAttribute,
            @RequestParam(value = "direction", defaultValue = "desc") String direction
    ) {
        Page<TransactionResponseDTO> transactionResponseDTOPage =
                    transactionService.findAllByAuthenticationAndPageAndDirectionAndSortAttribute(
                            authentication, pageNumber, pageSize, direction, sortAttribute
                    );
        return ResponseEntity.status(HttpStatus.OK).body(transactionResponseDTOPage);
    }

    @GetMapping("/search/{user-id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> findBy(
            @PathVariable(value = "user-id") UUID userID,
            @RequestParam(value = "page-size", defaultValue = "20")
            @Min(value = 1) @Max(value = 20)
            Integer pageSize,
            @RequestParam(value = "page-number", defaultValue = "1")
            @Min(value = 1)
            Integer pageNumber,
            @RequestParam(value = "sort", required = false, defaultValue = "createdDate")
            String sortAttribute,
            @RequestParam(value = "direction", defaultValue = "desc") String direction
    ) {
        Page<TransactionResponseDTO> transactionResponseDTOPage =
                    transactionService.findAllByUserIdAndPageAndDirectionAndSortAttribute(
                            userID, pageNumber, pageSize, direction, sortAttribute
                    );
        return ResponseEntity.status(HttpStatus.OK).body(transactionResponseDTOPage);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> findAllBy(
            @RequestParam(value = "page-size", defaultValue = "20")
            @Min(value = 1) @Max(value = 20)
            Integer pageSize,
            @RequestParam(value = "page-number", defaultValue = "1")
            @Min(value = 1)
            Integer pageNumber,
            @RequestParam(value = "sort", required = false, defaultValue = "created-date")
            String sortAttribute,
            @RequestParam(value = "direction", defaultValue = "desc") String direction
    ) {
        Page<TransactionResponseDTO> transactionResponseDTOPage =
                    transactionService.findAllByPageAndDirectionAndSortAttribute(
                            pageNumber, pageSize, direction, sortAttribute
                    );
        return ResponseEntity.status(HttpStatus.OK).body(transactionResponseDTOPage);
    }
}
