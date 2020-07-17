package com.epam.esm.repository.jpa;

import com.epam.esm.entity.User;

import java.util.List;

public interface UserRepository {
    void delete(long id);

    User findById(long id);

    List<User> findAll(int offset, int limit);

    User create(User user);

    int getUsersCount();
}
