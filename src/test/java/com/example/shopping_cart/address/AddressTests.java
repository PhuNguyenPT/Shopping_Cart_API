package com.example.shopping_cart.address;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class AddressTests {

    @Test
    public void testAddressGettersAndSetters() {
        Address address = new Address();
        address.setHouseNumber("123");
        address.setStreetName("Main St");
        address.setWardName("Ward 1");
        address.setCity("Cityville");
        address.setZipCode("12345");

        assertEquals("123", address.getHouseNumber());
        assertEquals("Main St", address.getStreetName());
        assertEquals("Ward 1", address.getWardName());
        assertEquals("Cityville", address.getCity());
        assertEquals("12345", address.getZipCode());
    }
}
