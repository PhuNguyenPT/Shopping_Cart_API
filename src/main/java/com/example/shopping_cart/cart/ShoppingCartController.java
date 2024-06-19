package com.example.shopping_cart.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @PostMapping("/upload/{user-id}")
    public ResponseEntity<?> upload(
            @PathVariable("user-id") UUID userId,
            @RequestBody
            List<ShoppingCartRequestDTO> shoppingCartRequestDTOList
    ) {
        ShoppingCartResponseDTO shoppingCartResponseDTO = shoppingCartService.save(userId, shoppingCartRequestDTOList);
        return ResponseEntity.status(HttpStatus.OK).body(shoppingCartResponseDTO);
    }
}
