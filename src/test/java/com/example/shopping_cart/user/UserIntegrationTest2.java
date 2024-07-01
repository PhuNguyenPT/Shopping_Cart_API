package com.example.shopping_cart.user;

import com.example.shopping_cart.authentication.AuthenticationService;
import com.example.shopping_cart.authentication.RegistrationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserIntegrationTest2 {

    @Autowired
    private AuthenticationService authenticationService;

    @MockBean
    private MyUserRepository myUserRepository;

    private MyUserService myUserService;

    @Test
    void registerUserWithInvalidEmail() {
        // Given
        var request = RegistrationRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("invalidEmail") // This email is not well-formatted
                .password("password")
                .build();

        // When & Then
        assertThrows(RuntimeException.class, () -> authenticationService.register(request));
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
}