package com.example.shopping_cart.user;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

public class TokenTests {

    @Test
    public void testTokenGettersAndSetters() {
        Token token = new Token();
        LocalDateTime now = LocalDateTime.now();
        token.setValue("token-value");
        token.setCreatedAt(now);
        token.setExpiresAt(now.plusHours(1));

        assertEquals("token-value", token.getValue());
        assertEquals(now, token.getCreatedAt());
        assertEquals(now.plusHours(1), token.getExpiresAt());
    }
}
