package com.kuimov.pp.task313.repository;

import com.kuimov.pp.task313.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User getUserByEmail(String email);
}
