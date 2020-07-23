package com.epam.esm.repository.jpa;

import com.epam.esm.repository.ShopRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public abstract class ShopJPARepository<T> implements ShopRepository<T> {
    @PersistenceContext
    protected EntityManager entityManager;

    @Transactional
    public T create(T t) {
        entityManager.persist(t);
        return t;
    }
}
