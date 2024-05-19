package com.example.authservice.services.auth;


import com.example.authservice.models.auth.Role;
import com.example.authservice.repositories.auth.RoleRepository;
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
    public Role getCompanyRole() {
        var role = roleRepository.findByName("ROLE_COMPANY");
        if (role.isPresent()){
            return role.get();
        }
        else {
            var newRole = new Role();
            newRole.setName("ROLE_COMPANY");
            newRole = roleRepository.save(newRole);
            return newRole;
        }
    }
}