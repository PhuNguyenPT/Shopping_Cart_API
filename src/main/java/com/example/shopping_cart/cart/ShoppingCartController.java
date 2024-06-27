package com.example.shopping_cart.cart;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> upload(
            Authentication authentication,
            @RequestBody @Valid
            List<ShoppingCartRequestDTO> shoppingCartRequestDTOList
    ) {
        ShoppingCartResponseDTO shoppingCartResponseDTO = shoppingCartService.save(
                authentication, shoppingCartRequestDTOList
        );
        return ResponseEntity.status(HttpStatus.OK).body(shoppingCartResponseDTO);
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> findShoppingCart(
            Authentication authentication,
            @RequestParam(value = "page-size", defaultValue = "20")
            @Min(value = 1) @Max(value = 20)
            Integer pageSize,
            @RequestParam(value = "page-number", defaultValue = "1")
            @Min(value = 1)
            Integer pageNumber
    ) {
        ShoppingCartResponseDTOFind shoppingCartResponseDTOFind =
                shoppingCartService.findByPage(
                        authentication, pageNumber, pageSize
                );
        return ResponseEntity.status(HttpStatus.OK).body(shoppingCartResponseDTOFind);
    }

    @PatchMapping("/update")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> updateShoppingCart(
            Authentication authentication,
            @RequestBody List<ShoppingCartRequestDTO> shoppingCartRequestDTOList
    ) {
        ShoppingCartResponseDTO shoppingCartResponseDTO =
                shoppingCartService.updateBy(authentication, shoppingCartRequestDTOList);
        return ResponseEntity.status(HttpStatus.OK).body(shoppingCartResponseDTO);
    }
}
