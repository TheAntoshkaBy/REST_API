package com.epam.esm.repository.jpa.impl;

import com.epam.esm.constant.SQLRequests;
import com.epam.esm.entity.User;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.constant.ErrorTextMessageConstants;
import com.epam.esm.exception.entity.InvalidDataOutputMessage;
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
        int col = entityManager.createQuery(SQLRequests.DELETE_USER_BY_ID)
                .setParameter(1, id).executeUpdate();
        if(col == 0){
            throw new RepositoryException(new InvalidDataOutputMessage("User",
                    ErrorTextMessageConstants.NOT_FOUND_USER));
        }
    }

    @Override
    public User findById(long id) {
        User user =  entityManager.find(User.class, id);
        if(user == null){
            throw new RepositoryException(new InvalidDataOutputMessage("User",
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
    public int getUsersCount() {
        Long count = (Long) entityManager.createQuery(SQLRequests.FIND_COUNT_OF_USER).getSingleResult();
        return count.intValue();
    }
}
