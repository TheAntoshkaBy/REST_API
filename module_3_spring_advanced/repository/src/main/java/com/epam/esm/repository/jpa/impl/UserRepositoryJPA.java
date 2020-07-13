package com.epam.esm.repository.jpa.impl;

import com.epam.esm.constant.SQLRequests;
import com.epam.esm.entity.User;
import com.epam.esm.repository.jpa.ShopJPARepository;
import com.epam.esm.repository.jpa.UserRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class UserRepositoryJPA extends ShopJPARepository<User> implements UserRepository {
    @Override
    public void delete(long id) {
    }

    @Override
    public User findById(long id) {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> findAll() {
       return entityManager.createQuery(SQLRequests.FIND_ALL_USERS).getResultList();
    }
}
