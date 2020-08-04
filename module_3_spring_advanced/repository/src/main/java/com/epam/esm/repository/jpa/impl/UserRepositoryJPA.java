package com.epam.esm.repository.jpa.impl;

import com.epam.esm.constant.SQLRequests;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.constant.EntityNameConstant;
import com.epam.esm.exception.constant.ErrorTextMessageConstants;
import com.epam.esm.exception.entity.InvalidDataOutputMessage;
import com.epam.esm.repository.jpa.ShopJPARepository;
import com.epam.esm.repository.jpa.UserRepository;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.NoResultException;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryJPA extends ShopJPARepository<User> implements UserRepository {

    @Override
    public void delete(long id) {
        int col = entityManager.createQuery(SQLRequests.DELETE_USER_BY_ID)
            .setParameter(1, id).executeUpdate();
        if (col == 0) {
            throw new RepositoryException(new InvalidDataOutputMessage(EntityNameConstant.USER,
                ErrorTextMessageConstants.NOT_FOUND_USER));
        }
    }

    @Override
    public User findById(long id) {
        User user = entityManager.find(User.class, id);
        if (user == null) {
            throw new RepositoryException(new InvalidDataOutputMessage(EntityNameConstant.USER,
                ErrorTextMessageConstants.NOT_FOUND_USER));
        }

        return user;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> findAll(int offset, int limit) {
        return entityManager.createQuery(SQLRequests.FIND_ALL_USERS)
            .setFirstResult(offset)
            .setMaxResults(limit)
            .getResultList();
    }

    @Override
    public User findByLogin(String login) {
        User user;
        try {
            user = (User) entityManager.createQuery(SQLRequests.FIND_USER_BY_LOGIN)
                .setParameter(1, login).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

        return user;
    }

    @Override
    public User findByEmail(String email) {
        User user;
        try {
            user = (User) entityManager.createQuery(SQLRequests.FIND_USER_BY_EMAIL)
                .setParameter(1, email).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

        return user;
    }

    @Override
    public int getUsersCount() {
        Long count = (Long) entityManager.createQuery(SQLRequests.FIND_COUNT_OF_USER)
            .getSingleResult();

        return count.intValue();
    }

    @Override
    public User createWithRole(User user, Role role) {
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);
        entityManager.persist(user);

        return user;
    }
}
