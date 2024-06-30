package com.example.shopping_cart.role;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MyRoleServiceTest {

    @Mock
    private MyRoleRepository myRoleRepository;

    @InjectMocks
    private MyRoleService myRoleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByAuthorityOrElseReturnNull() {
        MyRole myRole = new MyRole();
        myRole.setAuthority("ROLE_USER");

        when(myRoleRepository.findByAuthority("ROLE_USER")).thenReturn(Optional.of(myRole));

        MyRole foundRole = myRoleService.findByAuthorityOrElseReturnNull("ROLE_USER");

        assertNotNull(foundRole);
        assertEquals("ROLE_USER", foundRole.getAuthority());

        when(myRoleRepository.findByAuthority("ROLE_ADMIN")).thenReturn(Optional.empty());

        MyRole notFoundRole = myRoleService.findByAuthorityOrElseReturnNull("ROLE_ADMIN");

        assertNull(notFoundRole);
    }

    @Test
    void save() {
        MyRole myRole = new MyRole();
        myRole.setAuthority("ROLE_USER");

        when(myRoleRepository.save(any(MyRole.class))).thenReturn(myRole);

        MyRole savedRole = myRoleService.save(myRole);

        assertNotNull(savedRole);
        assertEquals("ROLE_USER", savedRole.getAuthority());
    }

    @Test
    void findByAuthority() {
        MyRole myRole = new MyRole();
        myRole.setAuthority("USER");

        when(myRoleRepository.findByAuthority("USER")).thenReturn(Optional.of(myRole));

        MyRole foundRole = myRoleService.findByAuthority(MyRole.Value.valueOf("USER"));

        assertNotNull(foundRole);
        assertEquals("USER", foundRole.getAuthority());

        when(myRoleRepository.findByAuthority("USER")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> myRoleService.findByAuthority(MyRole.Value.valueOf("USER")));
    }
}