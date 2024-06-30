package com.example.shopping_cart.user;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MyUserServiceTest {

    @Mock
    private MyUserRepository myUserRepository;

    @InjectMocks
    private MyUserService myUserService;

    private MyUser user1;
    private MyUser user2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user1 = new MyUser();
        user1.setEmail("test1@example.com");

        user2 = new MyUser();
        user2.setEmail("test2@example.com");
    }

    @Test
    void testFindAll() {
        when(myUserRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        Pageable pageable = PageRequest.of(0, 10);
        Page<MyUserResponseDTO> result = myUserService.findAll(0, 10);

        assertEquals(2, result.getContent().size());
        verify(myUserRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();
        user1.setId(id);
        when(myUserRepository.findById(id)).thenReturn(Optional.of(user1));

        MyUser result = myUserService.findById(id);

        assertEquals(user1, result);
        verify(myUserRepository, times(1)).findById(id);
    }

    @Test
    void testFindByIdNotFound() {
        UUID id = UUID.randomUUID();
        when(myUserRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> myUserService.findById(id));
        verify(myUserRepository, times(1)).findById(id);
    }

    @Test
    void testFindUserAttributesById() {
        UUID id = UUID.randomUUID();
        user1.setId(id);
        MyUserResponseDTO userResponseDTO = MyUserMapper.toMyUserResponseDTOFind(user1);
        when(myUserRepository.findById(id)).thenReturn(Optional.of(user1));

        MyUserResponseDTO result = myUserService.findUserAttributesById(id);

        assertEquals(userResponseDTO.getId(), result.getId());
        verify(myUserRepository, times(1)).findById(id);
    }

    @Test
    void testFindUserAttributesByIdNotFound() {
        UUID id = UUID.randomUUID();
        when(myUserRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> myUserService.findUserAttributesById(id));
        verify(myUserRepository, times(1)).findById(id);
    }

    @Test
    void testFindByEmail() {
        String email = "test1@example.com";
        user1.setEmail(email);
        when(myUserRepository.findByEmail(email)).thenReturn(Optional.of(user1));

        MyUser result = myUserService.findByEmail(email);

        assertEquals(user1, result);
        verify(myUserRepository, times(1)).findByEmail(email);
    }

    @Test
    void testFindByEmailNotFound() {
        String email = "test2@example.com";
        when(myUserRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> myUserService.findByEmail(email));
        verify(myUserRepository, times(1)).findByEmail(email);
    }

    @Test
    void testFindByEmailOrElseReturnNull() {
        String email = "test1@example.com";
        user1.setEmail(email);
        when(myUserRepository.findByEmail(email)).thenReturn(Optional.of(user1));

        MyUser result = myUserService.findByEmailOrElseReturnNull(email);

        assertEquals(user1, result);
        verify(myUserRepository, times(1)).findByEmail(email);
    }

    @Test
    void testFindByEmailOrElseReturnNullNotFound() {
        String email = "test2@example.com";
        when(myUserRepository.findByEmail(email)).thenReturn(Optional.empty());

        MyUser result = myUserService.findByEmailOrElseReturnNull(email);

        assertNull(result);
        verify(myUserRepository, times(1)).findByEmail(email);
    }

    @Test
    void testFindByUserAuthentication() {
        String email = "test1@example.com";
        user1.setEmail(email);
        when(myUserRepository.findByEmail(email)).thenReturn(Optional.of(user1));

        Authentication authentication = new UsernamePasswordAuthenticationToken(user1, null);
        MyUser result = myUserService.findByUserAuthentication(authentication);

        assertEquals(user1, result);
        verify(myUserRepository, times(1)).findByEmail(email);
    }

    @Test
    void testFindByUserAuthenticationNotFound() {
        String email = "test2@example.com";
        user1.setEmail(email); // Change the email of the authenticated user
        when(myUserRepository.findByEmail(email)).thenReturn(Optional.empty());

        Authentication authentication = new UsernamePasswordAuthenticationToken(user1, null);
        assertThrows(EntityNotFoundException.class, () -> myUserService.findByUserAuthentication(authentication));
        verify(myUserRepository, times(1)).findByEmail(email);
    }

    @Test
    void testFindUserAttributesByUserAuthentication() {
        String email = "test1@example.com";
        user1.setEmail(email);
        when(myUserRepository.findByEmail(email)).thenReturn(Optional.of(user1));

        Authentication authentication = new UsernamePasswordAuthenticationToken(user1, null);
        MyUserResponseDTO result = myUserService.findUserAttributesByUserAuthentication(authentication);

        assertEquals(user1.getId(), result.getId());
        verify(myUserRepository, times(1)).findByEmail(email);
    }

    @Test
    void testFindUserAttributesByUserAuthenticationNotFound() {
        String email = "test2@example.com";
        user1.setEmail(email); // Change the email of the authenticated user
        when(myUserRepository.findByEmail(email)).thenReturn(Optional.empty());

        Authentication authentication = new UsernamePasswordAuthenticationToken(user1, null);
        assertThrows(EntityNotFoundException.class, () -> myUserService.findUserAttributesByUserAuthentication(authentication));
        verify(myUserRepository, times(1)).findByEmail(email);
    }

    @Test
    void testSave() {
        MyUser user = new MyUser();
        user.setEmail("test@example.com");

        when(myUserRepository.save(any(MyUser.class))).thenReturn(user);

        MyUser result = myUserService.save(user);

        assertEquals(user, result);
        verify(myUserRepository, times(1)).save(user);
    }
}