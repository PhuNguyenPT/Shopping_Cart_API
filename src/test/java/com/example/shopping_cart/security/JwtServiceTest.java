package com.example.shopping_cart.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.*;

import java.time.Instant;
import java.util.Collections;


import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.util.Optional;

class JwtServiceTest {

    @Mock
    private JwtProperties jwtProperties;

    @Mock
    private JwtEncoder jwtEncoder;

    @Mock
    private JwtDecoder jwtDecoder;

    @InjectMocks
    private JwtService jwtService;

    private UserDetails userDetails;

    @Captor
    private ArgumentCaptor<JwtEncoderParameters> jwtEncoderParametersCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDetails = new User("testUser", "password", Collections.singletonList(new SimpleGrantedAuthority("USER")));
    }

    @Test
    void testGenerateToken() {
        // Arrange
        Long jwtExpiration = 3600000L;
        when(jwtProperties.getExpiration()).thenReturn(jwtExpiration);

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuer("shopping-cart.com")
                .subject(userDetails.getUsername())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusMillis(jwtExpiration))
                .claim("authorities", Collections.singletonList("USER"))
                .build();

        Jwt jwt = mock(Jwt.class);
        when(jwt.getTokenValue()).thenReturn("testToken");
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt); // Use any() here

        // Act
        String token = jwtService.generateToken(userDetails);

        // Assert
        assertNotNull(token);
        assertEquals("testToken", token);

        // Verify that the encoder was called with the correct parameters
        ArgumentCaptor<JwtEncoderParameters> captor = ArgumentCaptor.forClass(JwtEncoderParameters.class);
        verify(jwtEncoder).encode(captor.capture());
        JwtEncoderParameters capturedParameters = captor.getValue();

        // Validate the captured parameters
        JwtClaimsSet capturedClaimsSet = capturedParameters.getClaims();
        assertEquals(jwtClaimsSet.getSubject(), capturedClaimsSet.getSubject());

        Instant jwtTime = jwtClaimsSet.getIssuedAt();
        Instant capturedTime = capturedClaimsSet.getIssuedAt();
        assertThat(capturedTime).isBetween(jwtTime.minus(Duration.ofMillis(500)), jwtTime.plus(Duration.ofMillis(500)));

        Instant jwtExpirationTime = jwtClaimsSet.getExpiresAt();
        Instant capturedExpirationTime = capturedClaimsSet.getExpiresAt();
        assertThat(capturedExpirationTime).isBetween(jwtExpirationTime.minus(Duration.ofMillis(500)), jwtExpirationTime.plus(Duration.ofMillis(500)));

        // Verify that the properties were accessed as expected
        verify(jwtProperties).getExpiration();
    }


    @Test
    void testIsTokenValid() {
        String token = "testToken";

        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn(userDetails.getUsername());
        when(jwt.getClaim("iss")).thenReturn("shopping-cart.com");
        when(jwt.getExpiresAt()).thenReturn(Instant.now().plusSeconds(3600));
        when(jwtDecoder.decode(token)).thenReturn(jwt);

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void testExtractIssuer() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaim("iss")).thenReturn("shopping-cart.com");

        String issuer = jwtService.extractIssuer(jwt);

        assertEquals("shopping-cart.com", issuer);
    }

    @Test
    void testExtractUsername() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn("testUser");

        String username = jwtService.extractUsername(jwt);

        assertEquals("testUser", username);
    }
}
