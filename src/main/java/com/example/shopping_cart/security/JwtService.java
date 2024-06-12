package com.example.shopping_cart.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class JwtService {
//    private final JwtDecoder jwtDecoder;
//    private final JwtEncoder jwtEncoder;
//    @Value("${application.security.jwt.public-key}")
//    private RSAPublicKey publicKey;
//    @Value("${application.security.jwt.private-key}")
//    private RSAPrivateKey privateKey;
//    @Value("${application.security.jwt.expiration}")
//    private final Long jwtExpiration;

    private final JwtProperties jwtProperties;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return buildToken(claims, userDetails, jwtProperties.getExpiration());
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            @NotNull UserDetails userDetails,
            long jwtExpiration) {
        var authorities = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        JwtClaimsSet jwtClaimsSet =JwtClaimsSet.builder()
                .issuer("shopping-cart.com")
                .subject(userDetails.getUsername())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusMillis(jwtExpiration))
                .claim("authorities", authorities)
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();

//        return Jwts.builder()
//                .issuer("shopping-cart.com")
//                .claims(extraClaims)
//                .subject(userDetails.getUsername())
//                .issuedAt(Date.from(Instant.now()))
//                .expiration(Date.from(Instant.now().plusMillis(jwtExpiration)))
//                .claim("authorities", authorities)
//                .signWith(jwtProperties.getPrivateKey())
//                .compact();
    }

    public boolean isTokenValid(String tokenValue, @NotNull UserDetails userDetails) {
        Jwt jwt = jwtDecoder.decode(tokenValue);
        final String username = extractUsername(jwt);
        final String issuer = extractIssuer(jwt);
        return issuer.equals("shopping-cart.com") &&
                (username.equals(userDetails.getUsername())) &&
                !isTokenExpired(jwt);
    }

    public String extractIssuer(Jwt jwt) {
        return jwt.getClaim("iss");
    }

    public String extractUsername(Jwt jwt) {
        return jwt.getSubject();
    }

    private boolean isTokenExpired(Jwt jwt) {
        return extractExpiration(jwt).isBefore(Instant.now());
    }

    private Instant extractExpiration(Jwt jwt) {
        return jwt.getExpiresAt();
    }
}
