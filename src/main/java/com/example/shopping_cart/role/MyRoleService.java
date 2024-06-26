package com.example.shopping_cart.role;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyRoleService {
    private final MyRoleRepository myRoleRepository;

    public MyRole findByAuthorityOrElseReturnNull(String authority) {
        return myRoleRepository.findByAuthority(authority)
                .orElse(null);
    }
    public MyRole save(MyRole myRole) {
        return myRoleRepository.save(myRole);
    }
    public MyRole findByAuthority(MyRole.Value authority) {
        return myRoleRepository.findByAuthority(authority.name())
                .orElseThrow(() -> new EntityNotFoundException("Role " + authority.name() + " not found"));
    }
}
