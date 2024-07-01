package com.example.shopping_cart.email;

import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class EmailServiceIntegrationTest {

    @Autowired
    private EmailService emailService;

    @Test
    public void testSendEmail() throws MessagingException {
        emailService.sendEmail(
                "test@example.com",
                "username",
                EmailTemplate.ACTIVATE_ACCOUNT,
                "http://confirmation.url",
                "activationCode",
                "subject"
        );
    }
}