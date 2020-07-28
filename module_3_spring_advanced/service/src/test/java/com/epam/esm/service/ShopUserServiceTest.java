package com.epam.esm.service;

import com.epam.esm.entity.User;
import com.epam.esm.pojo.UserPOJO;
import com.epam.esm.repository.jpa.RoleRepository;
import com.epam.esm.repository.jpa.UserRepository;
import com.epam.esm.repository.jpa.impl.RoleRepositoryJPA;
import com.epam.esm.repository.jpa.impl.UserRepositoryJPA;
import com.epam.esm.service.impl.ShopUserService;
import com.epam.esm.service.validator.UserValidator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doAnswer;

public class ShopUserServiceTest {
    private UserRepository repository;
    private RoleRepository roleRepository;
    private UserValidator userValidator;
    private UserService userService;

    private User user;
    private List<User> users;

    @Before
    public void init() {
        repository = mock(UserRepositoryJPA.class);
        roleRepository = mock(RoleRepositoryJPA.class);
        userValidator = mock(UserValidator.class);

        userService = new ShopUserService(repository,userValidator,roleRepository);
        user = new User("aa","bb","cc","dd",null,"@mail");
        User user2 = new User("ii","ff","gg","hh",null,"@mail");
        users = new ArrayList<>();
        users.add(user);
        users.add(user2);
    }

    @Test
    public void findAll() {
        when(repository.findAll(anyInt(),anyInt())).thenReturn(users);

        List<UserPOJO> userActual = userService.findAll(1,5);
        Assert.assertEquals(userActual.stream().map(UserPOJO::pojoToEntity).collect(Collectors.toList()), users);
    }

    @Test
    public void find()  {
        Long tagId = 1l;
        Long getFirst = 1l;
        user = users.get(getFirst.intValue());
        when(repository.findById(anyInt())).thenReturn(users.get(0));

        Assert.assertEquals(user, users.get(getFirst.intValue()));
    }

    @Test
    public void delete() {
        Long tagId = 2L;
        Long getFirst = 0L;

        doAnswer(invocation -> {
            Object id = invocation.getArgument(getFirst.intValue());
            assertEquals(tagId.intValue(), id);
            users.remove(tagId.intValue());
            return null;
        }).when(repository).findById(anyInt());

        List<User> expectedTags = users;

        userService.delete(tagId);

        assertEquals(expectedTags, users);
    }

    @Test
    public void create(){
        Long tagId = 2L;
        Long getFirst = 0L;

        doAnswer(invocation -> {
            Object id = invocation.getArgument(getFirst.intValue());
            assertEquals(tagId.intValue(), id);
            users.add(user);
            return null;
        }).when(repository).create(any(User.class));

        List<User> expectedTags = users;

        assertEquals(expectedTags, users);
    }
}
