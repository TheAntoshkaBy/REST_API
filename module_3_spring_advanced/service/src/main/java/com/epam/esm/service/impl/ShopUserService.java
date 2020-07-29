package com.epam.esm.service.impl;

import com.epam.esm.entity.User;
import com.epam.esm.pojo.UserPOJO;
import com.epam.esm.repository.jpa.RoleRepository;
import com.epam.esm.repository.jpa.UserRepository;
import com.epam.esm.service.UserService;
import com.epam.esm.service.support.ServiceSupporter;
import com.epam.esm.service.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopUserService implements UserService {
    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final UserValidator userValidator;

    @Autowired
    public ShopUserService(UserRepository repository, UserValidator userValidator, RoleRepository roleRepository) {
        this.repository = repository;
        this.userValidator = userValidator;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<UserPOJO> findAll(int page, int size) {
        page = ServiceSupporter.convertPaginationPageToDbOffsetParameter(page, size);

        List<User> userPOJOS = repository.findAll(--page, size);
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
        String nameRoleUser = "ROLE_USER";

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String actualPassword = user.getPassword();
        user.setPassword(bCryptPasswordEncoder.encode(actualPassword));
        userValidator.isCorrectUser(user);
        return new UserPOJO(repository.createWithRole(ServiceSupporter.convertUserPojoToUserEntity(user),
                roleRepository.findByRoleName(nameRoleUser)));
    }

    @Override
    public UserPOJO findByLogin(String login) {
        return new UserPOJO(repository.findByLogin(login));
    }

    @Override
    public int getUsersCount() {
        return repository.getUsersCount();
    }
}
