package com.kuimov.pp.task313.Service;

import com.kuimov.pp.task313.models.Role;
import com.kuimov.pp.task313.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
public class RoleServiceImp implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImp(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Set<Role> getAllRoles() {
        return new HashSet<>(roleRepository.findAll());
    }

    @Override
    public void add(Role role) {
        roleRepository.save(role);
    }

    @Override
    public Set<Role> getSetRoles(Set<String> roles) {
        return roleRepository.findAllByNameIn(roles);
    }
}