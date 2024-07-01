package com.example.shopping_cart.user;

import com.example.shopping_cart.authentication.AuthenticationService;
import com.example.shopping_cart.authentication.RegistrationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserIntegrationTest {

    @Autowired
    private MyUserService myUserService;

    @Autowired
    private MyUserRepository myUserRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        myUserRepository.deleteAll();
    }

    @Test
    void registerANewUser() {
        // Given
        var newrequest = RegistrationRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("JohnDoe@gmail.com")
                .password("password")
                .build();

        authenticationService.register(newrequest);

        var registeredUser = myUserService.findByEmail(newrequest.getEmail());
        assertNotNull(registeredUser, "Registered user should not be null");
        assertEquals(newrequest.getFirstName(), registeredUser.getFirstName(), "First names should match");
        assertEquals(newrequest.getLastName(), registeredUser.getLastName(), "Last names should match");
        assertEquals(newrequest.getEmail(), registeredUser.getEmail(), "Emails should match");
    }
//
//    @Test
//    void registerAnExistingUser() {
//        var existing = RegistrationRequest.builder()
//                .firstName("Early")
//                .lastName("Man")
//                .email("AlreadyThere@gmail.com")
//                .password("password")
//                .build();
//
//        authenticationService.register(existing);
//
//        // Given
//        var existRequest = RegistrationRequest.builder()
//                .firstName("Early")
//                .lastName("Man")
//                .email("AlreadyThere@gmail.com")
//                .password("password")
//                .build();
//
//        assertThrows(RuntimeException.class, () -> authenticationService.register(existRequest));
//    }
}
