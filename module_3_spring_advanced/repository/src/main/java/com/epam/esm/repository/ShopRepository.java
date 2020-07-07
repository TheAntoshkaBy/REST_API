package com.epam.esm.repository;

import java.util.List;

public interface ShopRepository<T> {
    void delete(long id);
    T findById(long id);
    List<T> findAll();
    T create(T t);
}
