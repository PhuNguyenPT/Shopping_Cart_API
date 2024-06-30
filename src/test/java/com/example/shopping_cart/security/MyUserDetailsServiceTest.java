package com.example.shopping_cart.security;

import com.example.shopping_cart.user.MyUser;
import com.example.shopping_cart.user.MyUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MyUserDetailsServiceTest {

    @Mock
    private MyUserRepository userRepository;

    @InjectMocks
    private MyUserDetailsService myUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        // Arrange
        String username = "testUser";
        MyUser myUser = mock(MyUser.class); // Mock MyUser instead of UserDetails
        when(myUser.getUsername()).thenReturn(username); // Assuming MyUser has a getUsername method
        when(userRepository.findByEmail(username)).thenReturn(Optional.of(myUser));

        // Act
        UserDetails result = myUserDetailsService.loadUserByUsername(username);

        // Assert
        assertEquals(myUser.getUsername(), result.getUsername()); // Adjust the assertion accordingly
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Arrange
        String username = "testUser";
        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> myUserDetailsService.loadUserByUsername(username));
    }
}