package com.epam.esm.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.epam.esm.entity.User;
import com.epam.esm.pojo.UserPOJO;
import com.epam.esm.repository.jpa.RoleRepository;
import com.epam.esm.repository.jpa.UserRepository;
import com.epam.esm.repository.jpa.impl.RoleRepositoryJPA;
import com.epam.esm.repository.jpa.impl.UserRepositoryJPA;
import com.epam.esm.service.impl.ShopUserService;
import com.epam.esm.service.support.impl.UserPojoConverter;
import com.epam.esm.service.validator.UserValidator;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ShopUserServiceTest {

    private UserRepository repository;
    private RoleRepository roleRepository;
    private UserValidator userValidator;
    private UserService userService;
    private UserPojoConverter converter;

    public static final int DEFAULT_PAGE = 1;
    public static final int DEFAULT_SIZE = 5;

    private User user;
    private List<User> users;

    @Before
    public void init() {
        repository = mock(UserRepositoryJPA.class);
        roleRepository = mock(RoleRepositoryJPA.class);
        userValidator = mock(UserValidator.class);
        converter = mock(UserPojoConverter.class);

        userService = new ShopUserService(repository, userValidator, roleRepository, converter);

        user = new User("aa", "bb", "cc", "dd", null,
                        "@mail");
        User user2 = new User("ii", "ff", "gg", "hh", null,
                              "@mail");

        users = new ArrayList<>();
        users.add(user);
        users.add(user2);
    }

    @Test
    public void findAll() {
        when(repository.findAll(anyInt(), anyInt())).thenReturn(users);

        List<UserPOJO> userActual = userService.findAll(DEFAULT_PAGE, DEFAULT_SIZE);

        Assert.assertEquals(userActual, users.stream()
                                             .map(UserPOJO::new)
                                             .collect(Collectors.toList()));
    }

    @Test
    public void find_UserId_FindUserById() {
        long getFirst = 1l;

        user = users.get((int) getFirst);

        when(repository.findById(anyInt())).thenReturn(users.get(0));

        assertEquals(user, users.get((int) getFirst));
    }

    @Test
    public void delete_UserId_DeleteUserById() {
        long tagId = 2L;
        long getFirst = 0L;

        doAnswer(invocation -> {
            Object id = invocation.getArgument((int) getFirst);
            assertEquals((int) tagId, id);
            users.remove((int) tagId);
            return null;
        }).when(repository).findById(anyInt());

        List<User> expectedTags = users;
        userService.delete(tagId);

        assertEquals(expectedTags, users);
    }

    @Test
    public void create_User_NewUser() {
        long tagId = 2L;
        long getFirst = 0L;

        doAnswer(invocation -> {
            Object id = invocation.getArgument((int) getFirst);
            assertEquals((int) tagId, id);
            users.add(user);
            return null;
        }).when(repository).create(any(User.class));

        List<User> expectedTags = users;

        assertEquals(expectedTags, users);
    }

    @Test
    public void findByLogin_Login_UserByLogin() {
        when(repository.findByLogin(anyString())).thenReturn(user);

        UserPOJO expectedUser = userService.findByLogin(anyString());

        assertEquals(expectedUser, new UserPOJO(user));
    }

    @Test
    public void getUsersCount_WithoutParams_AllUsersCount() {
        int count = 10;

        when(repository.getUsersCount()).thenReturn(count);

        int expectedCount = userService.getUsersCount();

        assertEquals(expectedCount, count);
    }
}
