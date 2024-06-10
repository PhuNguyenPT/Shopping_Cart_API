package com.example.shopping_cart.content;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class GreetingController {
    @GetMapping("/home")
    public ResponseEntity<?> handleWelcome() {
        return ResponseEntity.ok("Welcome to home!");
    }
    @GetMapping("")
    public ResponseEntity<?> redirectToHome() {
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .header(HttpHeaders.LOCATION, "https://localhost/api/home").build();
    }
    @GetMapping("/admin/home")
    public ResponseEntity<?> handleAdminHome() {
        return ResponseEntity.ok("Welcome to Admin home!");
    }
    @GetMapping("/user/home")
    public ResponseEntity<?> handleUserHome() {
        return ResponseEntity.ok("Welcome to User home!");
    }
}

