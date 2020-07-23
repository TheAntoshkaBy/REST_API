package com.epam.esm.repository.jpa;

import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;

import java.util.List;

public interface UserRepository {
    void delete(long id);

    User findById(long id);

    List<User> findAll(int offset, int limit);

    User create(User user);

    User createWithRole(User user, Role role);

    User findByLogin(String login);

    User findByEmail(String email);

    int getUsersCount();
}
