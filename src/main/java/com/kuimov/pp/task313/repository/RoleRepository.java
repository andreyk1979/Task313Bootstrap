package com.kuimov.pp.task313.repository;

import com.kuimov.pp.task313.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Set<Role> findAllByNameIn(Set<String> roles);
}
