package com.example.shopping_cart.authentication;

import com.example.shopping_cart.email.EmailService;
import com.example.shopping_cart.user.MyUser;
import com.example.shopping_cart.user.MyUserRepository;
import com.example.shopping_cart.user.Token;
import com.example.shopping_cart.user.TokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthenticationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MyUserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @MockBean
    private EmailService emailService;

    @BeforeEach
    void setUp() throws MessagingException {
//        userRepository.deleteAll();
//        tokenRepository.deleteAll();
        doNothing().when(emailService).sendEmail(any(), any(), any(), any(), any(), any());
    }

//    @Test
//    void testRegisterUser() throws Exception {
//        RegistrationRequest request = RegistrationRequest.builder()
//                .firstName("John")
//                .lastName("Doe")
//                .email("john.doe@example.com")
//                .password("password123")
//                .build();
//
//        mockMvc.perform(post("/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isAccepted());
//    }
//
//    @Test
//    void testLoginWithValidCredentials() throws Exception {
//        // Register a user first
//        RegistrationRequest registrationRequest = RegistrationRequest.builder()
//                .firstName("John")
//                .lastName("Doe")
//                .email("john.doe@example.com")
//                .password("password123")
//                .build();
//
//        mockMvc.perform(post("/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(registrationRequest)))
//                .andExpect(status().isAccepted());
//
//        // Now try to login with the registered user
//        AuthenticationRequest loginRequest = AuthenticationRequest.builder()
//                .email("john.doe@example.com")
//                .password("password123")
//                .build();
//
//        mockMvc.perform(post("/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.token").exists());
//    }

//    @Test
//    void testLoginWithInvalidCredentials() throws Exception {
//        AuthenticationRequest loginRequest = AuthenticationRequest.builder()
//                .email("invalid@example.com")
//                .password("invalidpassword")
//                .build();
//
//        mockMvc.perform(post("/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest)))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    void testActivateAccountWithValidToken() throws Exception {
//        // Register a user first
//        RegistrationRequest registrationRequest = RegistrationRequest.builder()
//                .firstName("John")
//                .lastName("Doe")
//                .email("john.doe@example.com")
//                .password("password123")
//                .build();
//
//        mockMvc.perform(post("/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(registrationRequest)))
//                .andExpect(status().isAccepted());
//
//        // Assume that the token value is "123456"
//        mockMvc.perform(get("/auth/activate-account")
//                        .param("token", "123456"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void testActivateAccountWithInvalidToken() throws Exception {
//        mockMvc.perform(get("/auth/activate-account")
//                        .param("token", "invalidtoken"))
//                .andExpect(status().isBadRequest());
//    }
}