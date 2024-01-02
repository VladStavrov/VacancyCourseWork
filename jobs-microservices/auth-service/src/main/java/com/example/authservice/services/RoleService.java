package com.example.authservice.services;


import com.example.authservice.models.Role;
import com.example.authservice.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getUserRole() {

        var role = roleRepository.findByName("ROLE_USER");
        if (role.isPresent()){
            return role.get();
        }
       else {
           var newRole = new Role();
           newRole.setName("ROLE_USER");
           newRole = roleRepository.save(newRole);
           return newRole;
        }
    }
}
