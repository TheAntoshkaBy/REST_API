package com.epam.esm.repository;

import java.util.List;

public interface ShopRepository<T> {

    void delete(long id);

    T findById(long id);

    List<T> findAll(int offset, int limit);

    T create(T t);
}
