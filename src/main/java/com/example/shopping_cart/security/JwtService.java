package com.example.shopping_cart.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
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
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(jwtExpiration)))
                .claim("authorities", authorities)
                .signWith(jwtProperties.getPrivateKey())
                .compact();
    }

    public boolean isTokenValid(String tokenValue, @NotNull UserDetails userDetails) {
        final String username =  extractUsername(tokenValue);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(tokenValue);
    }

    public String extractUsername(String tokenValue) {
        return extractClaim(tokenValue, Claims::getSubject);
    }

    private boolean isTokenExpired(String tokenValue) {
        return extractExpiration(tokenValue).before(Date.from(Instant.now()));
    }

    private Date extractExpiration(String tokenValue) {
        return extractClaim(tokenValue, Claims::getExpiration);
    }

    public <T> T extractClaim(String tokenValue, @NotNull Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(tokenValue);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String tokenValue) {
        return Jwts.parser()
                .decryptWith(jwtProperties.getPrivateKey())
                .build()
                .parseSignedClaims(tokenValue)
                .getPayload();
    }
}
