package com.epam.esm.service.impl;

import com.epam.esm.entity.User;
import com.epam.esm.pojo.UserPOJO;
import com.epam.esm.repository.jpa.UserRepository;
import com.epam.esm.repository.jpa.impl.UserRepositoryJPA;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopUserService implements UserService {
    private final UserRepository repository;

    @Autowired
    public ShopUserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<UserPOJO> findAll() {
        List<User> userPOJOS = repository.findAll();
        return userPOJOS
                .stream()
                .map(UserPOJO::new)
                .collect(Collectors.toList());
    }

    @Override
    public UserPOJO find(long id) {
        return null;
    }

    @Override
    public void delete(long id) {

    }

    @Override
    public UserPOJO create(UserPOJO user) {
        User user1 = repository.create(user.pojoToEntity());
        return new UserPOJO(user1);
    }
}
