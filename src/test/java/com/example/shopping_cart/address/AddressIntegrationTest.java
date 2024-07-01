package com.example.shopping_cart.address;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class AddressIntegrationTest {

    @Autowired
    private AddressMapper addressMapper;

    @Test
    public void testAddressCreation() {
        Address address = new Address("123", "Main St", "Ward 1", "City", "12345");
        assertEquals("123", address.getHouseNumber());
        assertEquals("Main St", address.getStreetName());
        assertEquals("Ward 1", address.getWardName());
        assertEquals("City", address.getCity());
        assertEquals("12345", address.getZipCode());
    }

    @Test
    public void testAddressToResponseDTO() {
        Address address = new Address("123", "Main St", "Ward 1", "City", "12345");
        AddressResponseDTO responseDTO = AddressMapper.toAddressResponseDTO(address);
        assertEquals(address.getHouseNumber(), responseDTO.getHouseNumber());
        assertEquals(address.getStreetName(), responseDTO.getStreetName());
        assertEquals(address.getWardName(), responseDTO.getWardName());
        assertEquals(address.getCity(), responseDTO.getCity());
        assertEquals(address.getZipCode(), responseDTO.getZipCode());
    }

    @Test
    public void testRequestDTOToAddress() {
        AddressRequestDTO requestDTO = new AddressRequestDTO("123", "Main St", "Ward 1", "City", "12345");
        Address address = AddressMapper.toAddress(requestDTO);
        assertEquals(requestDTO.getHouseNumber(), address.getHouseNumber());
        assertEquals(requestDTO.getStreetName(), address.getStreetName());
        assertEquals(requestDTO.getWardName(), address.getWardName());
        assertEquals(requestDTO.getCity(), address.getCity());
        assertEquals(requestDTO.getZipCode(), address.getZipCode());
    }
}