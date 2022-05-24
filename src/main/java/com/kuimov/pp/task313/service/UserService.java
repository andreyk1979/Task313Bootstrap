package com.kuimov.pp.task313.service;

import com.kuimov.pp.task313.models.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    void save(User user);

    void delete(User user);

    void edit(User user);

    User getUserById(long id);

    User getUserByEmail (String email);

}
