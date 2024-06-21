package com.example.shopping_cart.order;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;



@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> uploadOrder(
            Authentication authentication,
            @RequestBody OrderRequestDTO orderRequestDTO
    ) {
        return orderService.saveOrder(authentication, orderRequestDTO);
    }

    @GetMapping("/search/{order-id}")
    public ResponseEntity<?> searchOrder(
            @PathVariable("order-id")
            @NotNull(message = "Order id must not be null")
            Long orderId
    ) {
        return orderService.searchOrderById(orderId);
    }

    @DeleteMapping("/delete/{order-id}")
    public ResponseEntity<?> deleteOrder(
            @PathVariable("order-id")
            @NotNull(message = "Order id must not be null")
            Long orderId
    ) {
        return orderService.deleteBy(orderId);
    }


    @PatchMapping("update/{order-id}")
    public ResponseEntity<?> updateOrder(
            @PathVariable("order-id") Long orderId,
            @RequestBody @Valid OrderUpdateDTO orderUpdateDTO
    ) {
        return orderService.updateOrderAttributes(orderId, orderUpdateDTO);
    }
}
