package com.example.shopping_cart.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MyUserService {

    private final MyUserRepository myUserRepository;

    public Page<MyUserResponseDTO> findAll(
            @NotNull MyUserRequestDTO myUserRequestDTO
    ) {
        Pageable pageable = PageRequest.of(myUserRequestDTO.getPageNumber(), myUserRequestDTO.getPageSize());
        List<MyUser> myUsers = myUserRepository.findAll();
        if (myUsers.isEmpty()) {
            throw new EntityNotFoundException("No user(s) found");
        }
        List<MyUserResponseDTO> myUserResponseDTOList = myUsers.stream()
                .map(MyUserMapper::toMyUserResponseDTOFind)
                .toList();
        return new PageImpl<>(
                myUserResponseDTOList,
                pageable,
                myUserResponseDTOList.size()
        );
    }

    public MyUser findById(UUID id) {
        return myUserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No user with " + id + " found"));
    }

    public MyUserResponseDTO findUserAttributesById(UUID id) {
        MyUser foundUser = findById(id);
        return MyUserMapper.toMyUserResponseDTOFind(foundUser);
    }

    public MyUser findByEmail(String email) {
        return myUserRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));
    }

    public MyUser findByEmailOrElseReturnNull(String email) {
        return myUserRepository.findByEmail(email)
                .orElse(null);
    }

    public MyUser findByUserAuthentication(
            @NotNull Authentication authentication
    ) {
        MyUser myUser = (MyUser) authentication.getPrincipal();
        return myUserRepository.findByEmail(myUser.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public MyUserResponseDTO findUserAttributesByUserAuthentication(
            @NotNull Authentication authentication
    ) {
        MyUser authenticatedUser = findByUserAuthentication(authentication);
        return MyUserMapper.toMyUserResponseDTOFind(authenticatedUser);
    }

    public MyUser save(MyUser myUser) {
        return myUserRepository.save(myUser);
    }
}
