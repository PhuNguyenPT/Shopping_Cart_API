package com.example.shopping_cart.user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.UUID;

public class MyUserTests {

    @Test
    public void testUserGettersAndSetters() {
        UUID userId = UUID.randomUUID();
        LocalDate dob = LocalDate.of(1990, 1, 1);
        MyUser user = new MyUser();

        user.setId(userId);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setDateOfBirth(dob);
        user.setEmail("john.doe@example.com");
        user.setPassword("password");

        assertEquals(userId, user.getId());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals(dob, user.getDateOfBirth());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("password", user.getPassword());
    }

    @Test
    public void testGetFullName() {
        MyUser user = new MyUser();
        user.setFirstName("John");
        user.setLastName("Doe");

        assertEquals("John Doe", user.getFullName());
    }
}
