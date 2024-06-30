//package com.example.shopping_cart.authentication;
//
//import com.example.shopping_cart.email.EmailService;
//import com.example.shopping_cart.email.EmailTemplate;
//import com.example.shopping_cart.role.MyRoleRepository;
//import com.example.shopping_cart.security.JwtService;
//import com.example.shopping_cart.user.MyUser;
//import com.example.shopping_cart.user.MyUserRepository;
//import com.example.shopping_cart.user.Token;
//import com.example.shopping_cart.user.TokenRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//public class AuthenticationServiceTest {
//
//    @Autowired
//    private AuthenticationService authenticationService;
//
//    private RegistrationRequest registrationRequest;
//    private AuthenticationRequest authenticationRequest;
//
//    @BeforeEach
//    void setup() {
//        registrationRequest = new RegistrationRequest("firstname", "lastname", "test@gmail.com", "password");
////        registrationRequest.setFirstName("Test");
////        registrationRequest.setLastName("User");
////        registrationRequest.setEmail("test.user@example.com");
////        registrationRequest.setPassword("password");
//
//        authenticationRequest = new AuthenticationRequest("test@gmail.com", "password");
////        authenticationRequest.setEmail("test.user@example.com");
////        authenticationRequest.setPassword("password");
//    }
//
//    @Test
//    void testRegister() {
//        assertDoesNotThrow(() -> authenticationService.register(registrationRequest));
//    }
//
//    @Test
//    void testAuthenticate() {
//        assertNotNull(authenticationService.authenticate(authenticationRequest));
//    }
//
//    @Test
//    void testActivateAccount() {
//        assertDoesNotThrow(() -> authenticationService.activateAccount("testToken"));
//    }
//}

package com.example.shopping_cart.authentication;

import com.example.shopping_cart.email.EmailService;
import com.example.shopping_cart.email.EmailTemplate;
import com.example.shopping_cart.role.MyRole;
import com.example.shopping_cart.role.MyRoleRepository;
import com.example.shopping_cart.security.JwtService;
import com.example.shopping_cart.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthenticationServiceTest {

    @Mock
    private MyRoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MyUserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationProperties authenticationProperties;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Captor
    private ArgumentCaptor<MyUser> userCaptor;

    private UUID id = UUID.randomUUID();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister() throws Exception {
        // Arrange
        RegistrationRequest request = new RegistrationRequest("John", "Doe", "johndoe@example.com", "password");

        MyRole userRole = new MyRole();
        userRole.setAuthority("USER");

        when(roleRepository.findByAuthority("USER")).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        // Act
        authenticationService.register(request);

        // Assert
        verify(userRepository).save(userCaptor.capture());
        MyUser savedUser = userCaptor.getValue();

        String authString = authenticationProperties.getActivationUrl();

        assertEquals("John", savedUser.getFirstName());
        assertEquals("Doe", savedUser.getLastName());
        assertEquals("johndoe@example.com", savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertFalse(savedUser.isEnabled());

        verify(emailService).sendEmail(eq("johndoe@example.com"), eq("John Doe"), eq(EmailTemplate.ACTIVATE_ACCOUNT), eq(authString), anyString(), eq("Account activation"));
    }

    @Test
    public void testAuthenticate() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest("johndoe@example.com", "password");

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("johndoe@example.com");
        when(authenticationManager.authenticate(any())).thenReturn(auth);

        MyUser user = new MyUser();
        user.setEmail("johndoe@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        when(jwtService.generateToken(anyMap(), any())).thenReturn("jwtToken");

        // Act
        AuthenticationResponse response = authenticationService.authenticate(request);

        // Assert
        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
    }

    @Test
    public void testActivateAccount() {
        // Arrange
        String tokenValue = "activationToken";

        MyUser user = new MyUser();
        user.setId(id);
        user.setEmail("johndoe@example.com");

        Token token = new Token();
        token.setValue(tokenValue);
        token.setCreatedAt(LocalDateTime.now());
        token.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        token.setUser(user);

        when(tokenRepository.findByValue(tokenValue)).thenReturn(Optional.of(token));
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // Act
        authenticationService.activateAccount(tokenValue);

        // Assert
        assertTrue(user.isEnabled());
        verify(userRepository).save(user);
        verify(tokenRepository).save(token);
    }

    @Test
    public void testActivateAccountWithExpiredToken() {
        // Arrange
        String tokenValue = "expiredToken";

        MyUser user = new MyUser();
        user.setId(id);
        user.setEmail("johndoe@example.com");

        Token token = new Token();
        token.setValue(tokenValue);
        token.setCreatedAt(LocalDateTime.now().minusMinutes(30));  // Token created 30 minutes ago
        token.setExpiresAt(LocalDateTime.now().minusMinutes(15));  // Token expired 15 minutes ago
        token.setUser(user);

        when(tokenRepository.findByValue(tokenValue)).thenReturn(Optional.of(token));

        // Act & Assert
        assertThrows(DateTimeException.class, () -> authenticationService.activateAccount(tokenValue));
    }

    @Test
    public void testActivateAccountWithInvalidToken() {
        // Arrange
        String tokenValue = "invalidToken";

        when(tokenRepository.findByValue(tokenValue)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidBearerTokenException.class, () -> authenticationService.activateAccount(tokenValue));
    }
}