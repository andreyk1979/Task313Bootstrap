package com.example.demo.Service;

import com.example.demo.models.Role;
import com.example.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


@Service
public class RoleService {


    @Autowired
    RoleRepository roleRepository;


    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public void add(Role role) {
        roleRepository.save(role);
    }

    public Set<Role> getSetRoles(Set<String> roles) {
        return roleRepository.getSetRoles(roles);

    }

}
