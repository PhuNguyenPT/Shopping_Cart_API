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
        OrderResponseDTO orderResponseDTO = orderService.saveOrder(authentication, orderRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(orderResponseDTO);
    }

    @GetMapping("/search/{order-id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> searchOrder(
            Authentication authentication,
            @PathVariable("order-id")
            @NotNull(message = "Order id must not be null")
            Long orderId
    ) {
        OrderResponseDTO orderResponseDTO = orderService.findByIdAndAuthentication(authentication, orderId);
        return ResponseEntity.status(HttpStatus.OK).body(orderResponseDTO);
    }

    @DeleteMapping("/delete/{order-id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteOrder(
            @PathVariable("order-id")
            @NotNull(message = "Order id must not be null")
            Long orderId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.deleteBy(orderId));
    }


    @PatchMapping("update/{order-id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> updateOrder(
            Authentication authentication,
            @PathVariable("order-id") Long orderId,
            @RequestBody @Valid OrderUpdateDTO orderUpdateDTO
    ) {
        OrderResponseDTO orderResponseDTO = orderService.updateOrderAttributes(authentication, orderId, orderUpdateDTO);
        return ResponseEntity.status(HttpStatus.OK).body(orderResponseDTO);
    }
}
