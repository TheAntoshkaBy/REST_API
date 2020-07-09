package com.epam.esm.repository.jpa;

import com.epam.esm.entity.User;

import java.util.List;

public interface UserRepository {
    void delete(long id);

    User findById(long id);

    List<User> findAll();

    User create(User user);
}
