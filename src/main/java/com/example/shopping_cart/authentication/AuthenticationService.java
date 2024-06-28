package com.example.shopping_cart.authentication;

import com.example.shopping_cart.email.EmailService;
import com.example.shopping_cart.email.EmailTemplate;
import com.example.shopping_cart.role.MyRoleRepository;
import com.example.shopping_cart.security.JwtService;
import com.example.shopping_cart.user.MyUser;
import com.example.shopping_cart.user.MyUserRepository;
import com.example.shopping_cart.user.Token;
import com.example.shopping_cart.user.TokenRepository;
import jakarta.mail.MessagingException;
import org.jetbrains.annotations.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final MyRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final MyUserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final AuthenticationProperties authenticationProperties;

    public void register(@NotNull RegistrationRequest request) {
        var userRole = roleRepository.findByAuthority("USER")
                .orElseThrow(() -> new IllegalStateException("ROLE USER was not initialized"));
        var user = MyUser.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .createdBy(request.getFirstName() + " " + request.getLastName())
                .isAccountNonLocked(true)
                .isAccountNonExpired(true)
                .isCredentialsNonExpired(true)
                .isEnabled(false)
                .roles(List.of(userRole))
                .build();
        userRepository.save(user);
        sendValidationEmail(user);
    }

    public void sendValidationEmail(MyUser user) {
        var newToken = generateAndSaveActivationToken(user);
        // send email
        try {
            emailService.sendEmail(
                    user.getEmail(),
                    user.getFullName(),
                    EmailTemplate.ACTIVATE_ACCOUNT,
                    authenticationProperties.getActivationUrl(),
                    newToken,
                    "Account activation"
            );
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private String generateAndSaveActivationToken(MyUser user) {
        String generatedToken = generateActivationCode();
        var token = Token.builder()
                .value(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    @NotNull
    private String generateActivationCode() {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < 6; i++) {
            int randomIndex = secureRandom.nextInt(characters.length()); // 0..9
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }

    public AuthenticationResponse authenticate(
            @NotNull AuthenticationRequest request
    ) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        Map<String, Object> claims = new HashMap<>();
        Optional<MyUser> user = userRepository.findByEmail(auth.getName());

        if (user.isPresent()) {
            claims.put("fullName", user.get().getFullName());
            var jwt = jwtService.generateToken(claims, user.get());
            return AuthenticationResponse.builder()
                    .token(jwt)
                    .build();
        } else {
            // Handle the case where the user is not found
            throw new UsernameNotFoundException("User not found with email: " + request.getEmail());
        }
    }

//    @Transactional
    public void activateAccount(String token) {
        Token savedToken = tokenRepository.findByValue(token)
                .orElseThrow(() -> new InvalidBearerTokenException("Invalid token"));
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new DateTimeException("Activation token has expired. " +
                    "A new token has been sent to the same registered email address");
        }
        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }
}
