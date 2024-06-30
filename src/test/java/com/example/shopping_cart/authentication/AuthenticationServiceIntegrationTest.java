//package com.example.shopping_cart.authentication;
//
//import com.example.shopping_cart.user.MyUser;
//import com.example.shopping_cart.user.MyUserRepository;
//import com.example.shopping_cart.user.Token;
//import com.example.shopping_cart.user.TokenRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//public class AuthenticationServiceIntegrationTest {
//
//        @Autowired
//        private AuthenticationService authenticationService;
//
//        @Autowired
//        private MyUserRepository userRepository;
//
//        @Autowired
//        private TokenRepository tokenRepository;
//
//        @Test
//        void testRegister() {
//            // Given
//            RegistrationRequest request = RegistrationRequest.builder()
//                    .firstName("John")
//                    .lastName("Doe")
//                    .email("test@gmail.com")
//                    .password("password123")
//                    .build();
//
//            authenticationService.register(request);
//
//            assertEquals(1, userRepository.count());
//            assertEquals(1, tokenRepository.count());
//        }
//}