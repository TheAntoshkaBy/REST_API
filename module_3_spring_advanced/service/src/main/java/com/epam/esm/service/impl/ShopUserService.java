package com.epam.esm.service.impl;

import com.epam.esm.entity.User;
import com.epam.esm.pojo.UserPOJO;
import com.epam.esm.repository.jpa.UserRepository;
import com.epam.esm.service.UserService;
import com.epam.esm.service.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopUserService implements UserService {
    private final UserRepository repository;
    private UserValidator userValidator;

    @Autowired
    public ShopUserService(UserRepository repository, UserValidator userValidator) {
        this.repository = repository;
        this.userValidator = userValidator;
    }

    @Override
    public List<UserPOJO> findAll(int page, int size) {
        if (page != 1) {
            page = size * (page - 1) + 1;
        }
        List<User> userPOJOS = repository.findAll(page, size);
        return userPOJOS
                .stream()
                .map(UserPOJO::new)
                .collect(Collectors.toList());
    }

    @Override
    public UserPOJO find(long id) {
        return new UserPOJO(repository.findById(id));
    }

    @Override
    public void delete(long id) {
        repository.delete(id);
    }

    @Override
    public UserPOJO create(UserPOJO user) {
        userValidator.isCorrectUser(user);
        return new UserPOJO(repository.create(user.pojoToEntity()));
    }

    @Override
    public int getUsersCount() {
        return repository.getUsersCount();
    }
}
