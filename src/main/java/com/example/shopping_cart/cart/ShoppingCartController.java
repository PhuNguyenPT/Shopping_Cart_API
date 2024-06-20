package com.example.shopping_cart.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(
            Authentication authentication,
            @RequestBody
            List<ShoppingCartRequestDTO> shoppingCartRequestDTOList
    ) {
        ShoppingCartResponseDTO shoppingCartResponseDTO = shoppingCartService.save(authentication, shoppingCartRequestDTOList);
        return ResponseEntity.status(HttpStatus.OK).body(shoppingCartResponseDTO);
    }
}
