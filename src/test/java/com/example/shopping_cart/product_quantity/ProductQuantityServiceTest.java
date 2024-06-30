package com.example.shopping_cart.product_quantity;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ProductQuantityServiceTest {

    @Mock
    private ProductQuantityRepository productQuantityRepository;

    @InjectMocks
    private ProductQuantityService productQuantityService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById() {
        ProductQuantity productQuantity = new ProductQuantity();
        when(productQuantityRepository.findById(1L)).thenReturn(Optional.of(productQuantity));

        ProductQuantity result = productQuantityService.findById(1L);

        assertEquals(productQuantity, result);
        verify(productQuantityRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindByIdNotFound() {
        when(productQuantityRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productQuantityService.findById(1L));

        verify(productQuantityRepository, times(1)).findById(1L);
    }

    @Test
    public void testDeleteById() {
        doNothing().when(productQuantityRepository).deleteById(1L);

        productQuantityService.deleteById(1L);

        verify(productQuantityRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testSave() {
        ProductQuantity productQuantity = new ProductQuantity();
        when(productQuantityRepository.save(productQuantity)).thenReturn(productQuantity);

        ProductQuantity result = productQuantityService.save(productQuantity);

        assertEquals(productQuantity, result);
        verify(productQuantityRepository, times(1)).save(productQuantity);
    }
}