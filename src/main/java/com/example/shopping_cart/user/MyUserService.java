package com.example.shopping_cart.user;

import com.example.shopping_cart.address.AddressMapper;
import com.example.shopping_cart.authentication.AuthenticationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MyUserService {

    private final MyUserRepository myUserRepository;
    private final AuthenticationService authenticationService;

    public Page<MyUserResponseDTO> findAll(
            Integer pageNumber,
            Integer pageSize
    ) {

        List<MyUser> myUsers = myUserRepository.findAll();
        if (myUsers.isEmpty()) {
            throw new EntityNotFoundException("No user(s) found");
        }
        List<MyUserResponseDTO> myUserResponseDTOList = myUsers.stream()
                .map(MyUserMapper::toMyUserResponseDTOFind)
                .toList();

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

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

    @Transactional
    public MyUser save(MyUser myUser) {
        return myUserRepository.save(myUser);
    }

    @Transactional
    public MyUserResponseDTO updateUserAttributesByAuthentication(
            Authentication authentication,
            @NotNull MyUserRequestDTO myUserRequestDTO
    ) {
        MyUser authenticatedUser = findByUserAuthentication(authentication);
        if (myUserRequestDTO.getFirstName() != null) {
            authenticatedUser.setFirstName(myUserRequestDTO.getFirstName());
        }
        if (myUserRequestDTO.getLastName() != null) {
            authenticatedUser.setLastName(myUserRequestDTO.getLastName());
        }
        if (myUserRequestDTO.getFirstName() != null &&
                myUserRequestDTO.getLastName() != null) {
            authenticatedUser.setLastModifyBy(authenticatedUser.getFullName());
        }
        if (myUserRequestDTO.getPhoneNumber() != null) {
            authenticatedUser.setPhoneNumber(myUserRequestDTO.getPhoneNumber());
        }
        if (myUserRequestDTO.getDateOfBirth() != null) {
            authenticatedUser.setDateOfBirth(myUserRequestDTO.getDateOfBirth());
        }
        if (myUserRequestDTO.getAddressRequestDTO() != null) {
            authenticatedUser.setAddress(AddressMapper.toAddress(myUserRequestDTO.getAddressRequestDTO()));
        }
        if (myUserRequestDTO.getEmail() != null &&
                !myUserRequestDTO.getEmail().equalsIgnoreCase(authenticatedUser.getEmail())
        ) {
            authenticatedUser.setEnabled(false);
            authenticationService.sendValidationEmail(authenticatedUser);
            authenticatedUser.setEmail(myUserRequestDTO.getEmail());
        }

        MyUserResponseDTO myUserResponseDTO = MyUserMapper.toMyUserResponseDTO(authenticatedUser);
        if (!authenticatedUser.isEnabled()) {
            myUserResponseDTO.setMessage("Please activate your account");
        } else {
            myUserResponseDTO.setMessage("Update successfully");
        }
        return myUserResponseDTO;
    }
}
