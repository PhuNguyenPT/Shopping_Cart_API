package com.example.shopping_cart.authentication;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class AuthenticationProperties {
    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;
}
